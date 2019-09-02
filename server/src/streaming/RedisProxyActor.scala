package monarchy.streaming

import akka.actor.{Actor, ActorRef}

class RedisProxyActor extends Actor {
  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case s: String => next.foreach(_ ! Redis(s))
  }
}
