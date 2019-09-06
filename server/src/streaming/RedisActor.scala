package monarchy.streaming

import akka.actor.ActorRef
import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}

object RedisActor {
  import StreamingChannel._
  val Channels = Seq(Public, Matchmaking)
  val Patterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    ok => println(s"connected: $ok")
  }
}

import RedisActor._
case class RedisActor(inet: InetSocketAddress, proxyRef: ActorRef)
  extends RedisSubscriberActor(inet, Channels, Patterns, onConnectStatus = ConnectionCallback) {

  // See definitions for message types here:
  // https://github.com/etaty/rediscala/blob/master/src/main/scala/redis/api/pubsub/pubsub.scala
  override def onMessage(m: Message) = {
    proxyRef ! RedisPub(m.channel, m.data.utf8String)
  }

  override def onPMessage(m: PMessage) = {
    proxyRef ! RedisPub(m.channel, m.data.utf8String)
  }
}

object StreamingChannel {
  def base(suffix: String) = s"monarchy/streaming/$suffix"
  final val Public = base("public")
  final val Matchmaking = base("matchmaking")
}
