package monarchy.streaming

import akka.actor.{Actor, ActorRef}
import monarchy.auth._
import monarchy.util.Json

class RedisProxyActor(auth: Auth) extends Actor {
  import StreamingChannel._

  var next: Option[ActorRef] = None
  val personal = auth match {
    case NullAuth => "unused"
    case Authenticated(u) => StreamingChannel.personal(u.id)
  }

  def receive = {
    case Connect(ref) => next = Some(ref)
    case RedisPub(channel, text) =>
      channel match {
        case `personal` =>
          next.foreach(_ ! Json.parse[Personal](text))
        case StreamingChannel.Matchmaking =>
          next.foreach(_ ! Json.parse[Matchmaking](text))
        case _ =>
          next.foreach(_ ! RedisRaw(text))
      }
  }
}
