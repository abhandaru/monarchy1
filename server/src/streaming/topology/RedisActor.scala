package monarchy.streaming.topology

import akka.actor.ActorRef
import java.net.InetSocketAddress
import monarchy.auth.{Auth, Authenticated, NullAuth}
import monarchy.streaming.core._
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}

object RedisActor {
  import StreamingChannel._
  val DefualtChannels = Seq(Public, Matchmaking)
  val DefaultPatterns = Nil
  val ConnectionCallback: Boolean => Unit = {
    ok => println(s"[redis-actor] connected: $ok")
  }

  def channels(auth: Auth): Seq[String] = DefualtChannels

  def patterns(auth: Auth) = auth match {
    case NullAuth => DefaultPatterns
    case a: Authenticated => DefaultPatterns ++ Seq(gamePattern(a.userId))
  }
}

import RedisActor._
case class RedisActor(inet: InetSocketAddress, proxyRef: ActorRef, auth: Auth)
  extends RedisSubscriberActor(inet, channels(auth), patterns(auth), onConnectStatus = ConnectionCallback) {

  // See definitions for message types here:
  // https://github.com/etaty/rediscala/blob/master/src/main/scala/redis/api/pubsub/pubsub.scala
  override def onMessage(m: Message) = {
    proxyRef ! RedisPub(m.channel, m.data.utf8String)
  }

  override def onPMessage(m: PMessage) = {
    proxyRef ! RedisPub(m.channel, m.data.utf8String, Some(m.patternMatched))
  }
}
