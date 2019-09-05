package monarchy.streaming

import akka.actor.{Actor, ActorRef}
import monarchy.auth.Authenticated
import monarchy.util.Async
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}

class ClientActor(implicit ec: ExecutionContext, redisCli: RedisClient) extends Actor {
  var next: Option[ActorRef] = None
  def receive = {
    case Connect(ref) => next = Some(ref)
    case Ping => next.foreach(_ ! Pong(System.currentTimeMillis))
    case ChallengeSeek(auth) => challengeSeek(auth)
  }

  def challengeSeek(auth: Authenticated): Future[_] = {
    Async.join(
      redisCli.set(StreamingKey.Challenge(auth.user.id), "true", exSeconds = Some(300)),
      redisCli.publish(StreamingChannel.Matchmaking, """{"foo":1}""")
    )
  }
}
