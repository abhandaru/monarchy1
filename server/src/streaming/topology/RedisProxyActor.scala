package monarchy.streaming.topology

import akka.actor.{Actor, ActorRef}
import monarchy.auth.{Auth, Authenticated, NullAuth}
import monarchy.streaming.core._
import monarchy.util.Json

class RedisProxyActor(auth: Auth) extends Actor {
  import StreamingChannel._

  var next: Option[ActorRef] = None
  val Seq(gameCreate, gameChange) = auth match {
    case NullAuth => "???"
    case Authenticated(u) => Seq(
      StreamingChannel.gameCreate(u.id),
      StreamingChannel.gameChange(u.id)
    )
  }

  def receive = {
    case Connect(ref) => next = Some(ref)
    case RedisPub(channel, text, _) =>
      channel match {
        case `gameCreate` =>
          next.foreach(_ ! Json.parse[GameCreate](text))
        case `gameChange` =>
          next.foreach(_ ! Json.parse[GameChange](text))
        case StreamingChannel.Matchmaking =>
          next.foreach(_ ! Json.parse[Matchmaking](text))
        case _ =>
          next.foreach(_ ! RedisRaw(text))
      }
  }
}
