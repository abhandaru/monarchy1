package monarchy.streaming

import akka.actor.ActorRef
import java.net.InetSocketAddress
import monarchy.auth._
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}

object RedisActor {
  import StreamingChannel._
  val DefualtChannels = Seq(Public, Matchmaking)
  val Patterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    ok => println(s"connected: $ok")
  }

  def channels(auth: Auth): Seq[String] = auth match {
    case NullAuth => DefualtChannels
    case Authenticated(u) => DefualtChannels ++ Seq(personal(u.id))
  }
}

import RedisActor._
case class RedisActor(inet: InetSocketAddress, proxyRef: ActorRef, auth: Auth)
  extends RedisSubscriberActor(inet, channels(auth), Patterns, onConnectStatus = ConnectionCallback) {

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

  def personal(userId: Long) = base(s"personal/$userId")
}
