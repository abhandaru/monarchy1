package monarchy.streaming.topology

import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage, Message}
import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._
import com.typesafe.scalalogging.StrictLogging
import java.net.InetSocketAddress
import monarchy.auth.Auth
import monarchy.streaming.core._
import monarchy.streaming.format.ActionRendererProxy
import monarchy.streaming.process.ClientActionProxy
import redis.RedisClient
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

case class MessageTopologyBuilder(
  redisAddr: InetSocketAddress,
  auth: Auth
)(implicit
  ec: ExecutionContext,
  actorSys: ActorSystem,
  actionRendererProxy: ActionRendererProxy,
  clientActionProxy: ClientActionProxy,
  materializer: ActorMaterializer,
  redisClient: RedisClient
) extends StrictLogging {
  def build: Flow[Message, Message, _] = {
    val clientRef = actorSys.actorOf(Props(new ClientActor))
    val redisProxyRef = actorSys.actorOf(Props(new RedisProxyActor(auth)))
    val redisRef = actorSys.actorOf(Props(RedisActor(redisAddr, redisProxyRef, auth)))

    val incomingSink: Sink[Message, NotUsed] = {
      Flow[Message]
        .mapConcat(drainNonText)
        .mapAsync(1)(resolveText)
        .mapConcat(ActionExtractor(auth, _))
        .log("message-topology-builder.sink")
        .to(Sink.actorRef[StreamAction](clientRef, PoisonPill))
    }

    val outgoingSource: Source[Message, NotUsed] = {
      Source.actorRef[StreamAction](10, OverflowStrategy.fail)
        .mapMaterializedValue { ref =>
          clientRef ! Connect(ref)
          redisProxyRef ! Connect(ref)
          NotUsed
        }
        .mapAsync(1)(actionRendererProxy)
        .log("message-topology-builder.source")
        .collect(liftAsMessage)
    }

    // then combine both to a flow
    Flow.fromSinkAndSource(incomingSink, outgoingSource)
  }

  def drainNonText(m: Message): List[TextMessage] = m match {
    case tm: TextMessage => List(tm)
    case bm: BinaryMessage =>
      // Ignore binary messages but drain content to avoid the stream being clogged.
      bm.dataStream.runWith(Sink.ignore)
      Nil
  }

  def resolveText(tm: TextMessage): Future[String] = {
    tm.toStrict(5.seconds).map(_.text)
      .map { t => logger.info(s"[message-topology-builder] message=$t"); t }
  }

  def liftAsMessage: PartialFunction[Option[String], TextMessage] = {
    case Some(s) => TextMessage(s)
  }
}
