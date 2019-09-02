package monarchy.streaming

import akka.actor.ActorRef

sealed trait Action

case object Ping extends Action
case class Pong(at: Long) extends Action
case class Connect(next: ActorRef) extends Action
case class Redis(text: String) extends Action

