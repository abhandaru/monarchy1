package monarchy.streaming.topology

import akka.actor.{Actor, ActorRef}
import monarchy.auth.Authenticated
import monarchy.streaming.core._
import monarchy.streaming.process.ClientActionProxy
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}

class ClientActor(implicit
  ec: ExecutionContext,
  clientActionProxy: ClientActionProxy,
  redisCli: RedisClient) extends Actor {
  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case Ping => next.foreach(_ ! Pong(System.currentTimeMillis))
    case axn: StreamAction => clientActionProxy(axn)
  }
}
