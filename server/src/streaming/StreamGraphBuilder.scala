package monarchy.streaming

import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage, Message}
import akka.stream.actor.ActorPublisher
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Source, Flow, Sink}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import java.net.InetSocketAddress

import akka.stream._
import akka.stream.stage._
import java.util.concurrent.ConcurrentLinkedQueue

import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActorWithCallback
import redis.api.pubsub.{PMessage => RedisPatternMessage, Message => RedisMessage}


case class RedisSource(redisAddr: InetSocketAddress)(implicit actorSys: ActorSystem)
  extends GraphStage[SourceShape[String]] {

  // Define the (sole) output port of this stage
  val out: Outlet[String] = Outlet("RedisSource")

  // Define the shape of this stage, which is SourceShape with the port we defined above
  override val shape: SourceShape[String] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      val queue = new ConcurrentLinkedQueue[String]

      def onMessage(m: RedisMessage) = {
        val RedisMessage(channel, data) = m
        queue.offer(s"redis/$channel >> ${m.data.utf8String}")
      }

      def onPatternMessage(m: RedisPatternMessage) = {
        val RedisPatternMessage(matched, channel, data) = m
        queue.offer(s"redis/$channel >> ${data.utf8String}")
      }

      val subRef = actorSys.actorOf(
        Props[RedisSubscriber](RedisSubscriber(redisAddr, onMessage, onPatternMessage))
          .withDispatcher("rediscala.rediscala-client-worker-dispatcher")
      )

      setHandler(out, new OutHandler {
        override def onPull(): Unit = Option(queue.poll).foreach(push(out, _))
        override def onDownstreamFinish(): Unit = {
          super.onDownstreamFinish()
          complete(out)
        }
      })
    }
  }
}

object RedisSubscriber {
  val Channels = Seq("ping", "matchmaking")
  val Patterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    connected => println(s"connected: $connected")
  }
}

import RedisSubscriber._
case class RedisSubscriber(
  inet: InetSocketAddress,
  onMessage: RedisMessage => Unit,
  onPatternMessage: RedisPatternMessage => Unit,
) extends RedisSubscriberActorWithCallback(
  address = inet,
  channels = Channels,
  patterns = Patterns,
  messageCallback = onMessage,
  pmessageCallback = onPatternMessage,
  onConnectStatus = ConnectionCallback
)
