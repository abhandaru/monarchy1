package monarchy.streaming

import akka.actor.{Actor, ActorRef}
import monarchy.util.Json

class RedisProxyActor extends Actor {
  import StreamingChannel._

  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case RedisPub(channel, text) =>
      channel match {
        case Matchmaking => next.foreach(_ ! Json.parse[Matchmaking](text))
        case _ => next.foreach(_ ! RedisRaw(text))
      }
  }
}

object StreamingChannel {
  def base(suffix: String) = s"monarchy/streaming/$suffix"
  final val Public = base("public")
  final val Matchmaking = base("matchmaking")
}
