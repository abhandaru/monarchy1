package monarchy.streaming

import akka.actor.{Actor, ActorRef}

class ClientActor extends Actor {
  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case Ping => next.foreach(_ ! Pong(System.currentTimeMillis))
  }
}
