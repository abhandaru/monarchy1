package monarchy.streaming

import akka.actor.ActorRef
import monarchy.auth.Authenticated

sealed trait Action

// Internal actions
case class Connect(next: ActorRef) extends Action
case class Redis(text: String) extends Action

// Inbound actions
case object Ping extends Action
case class ChallengeSeek(auth: Authenticated) extends Action

// Outbound actions
case class Pong(at: Long) extends Action


