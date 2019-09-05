package monarchy.streaming

import akka.actor.ActorRef
import monarchy.auth.Authenticated

sealed trait StreamAction

// Internal actions
case class Connect(next: ActorRef) extends StreamAction
case class RedisPub(channel: String, text: String) extends StreamAction

// Inbound actions
case object Ping extends StreamAction
case class ChallengeSeek(auth: Authenticated) extends StreamAction

// Outbound actions
case class Matchmaking(foo: Int) extends StreamAction
case class Pong(at: Long) extends StreamAction
case class RedisRaw(text: String) extends StreamAction


