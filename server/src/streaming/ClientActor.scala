package monarchy.streaming

import monarchy.auth.Authenticated
import akka.actor.{Actor, ActorRef}
import redis.RedisClient

class ClientActor(implicit redisCli: RedisClient) extends Actor {
  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case Ping => next.foreach(_ ! Pong(System.currentTimeMillis))
    case ChallengeSeek(auth) => challengeSeek(auth)
  }

  def challengeSeek(auth: Authenticated): Unit = {
    println(s"$auth is looking for a challenge")
  }
}
