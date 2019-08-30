package monarchy.streaming

import akka.stream.actor.ActorPublisher
import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}

object RedisActorPublisher {
  val Channels = Seq("ping", "matchmaking")
  val Patterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    connected => println(s"connected: $connected")
  }
}

import RedisActorPublisher._
case class RedisActorPublisher(inet: InetSocketAddress)
  extends RedisSubscriberActor(inet, Channels, Patterns, onConnectStatus = ConnectionCallback)
  with ActorPublisher[String] {

  // See definitions for message types here:
  // https://github.com/etaty/rediscala/blob/master/src/main/scala/redis/api/pubsub/pubsub.scala
  def onMessage(m: Message) = {
    val Message(channel, data) = m
    emit(s"redis/$channel >> ${data.utf8String}")
  }

  def onPMessage(m: PMessage) = {
    val PMessage(matched, channel, data) = m
    emit(s"redis/$channel >> ${data.utf8String}")
  }

  def emit(s: String): Unit = {
    if (isActive && totalDemand > 0) {
      onNext(s)
    }
  }
}
