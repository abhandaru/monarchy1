package monarchy.streaming

import akka.actor.ActorRef
import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}

object RedisActor {
  val Channels = Seq("ping", "matchmaking")
  val Patterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    connected => println(s"connected: $connected")
  }
}

import RedisActor._
case class RedisActor(inet: InetSocketAddress, proxyRef: ActorRef)
  extends RedisSubscriberActor(inet, Channels, Patterns, onConnectStatus = ConnectionCallback) {

  // See definitions for message types here:
  // https://github.com/etaty/rediscala/blob/master/src/main/scala/redis/api/pubsub/pubsub.scala
  def onMessage(m: Message) = {
    val Message(channel, data) = m
    proxyRef ! s"redis/${m.channel} >> ${m.data.utf8String}"
  }

  def onPMessage(m: PMessage) = {
    proxyRef ! s"redis/${m.channel} >> ${m.data.utf8String}"
  }
}
