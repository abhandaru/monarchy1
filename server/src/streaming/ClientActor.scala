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
    case ChallengeAccept(auth, body) => challengeAccept(auth, body)
    case ChallengeSeek(auth) => challengeSeek(auth)
    case ChallengeSeekCancel(auth) => challengeSeekCancel(auth)
  }

  def challengeSeek(auth: Authenticated): Future[_] = {
    Async.join(
      redisCli.set(StreamingKey.Challenge(auth.user.id), "true", exSeconds = Some(300)),
      redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}")
    )
  }

  def challengeSeekCancel(auth: Authenticated): Future[_] = {
    Async.join(
      redisCli.del(StreamingKey.Challenge(auth.user.id)),
      redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}")
    )
  }

  def challengeAccept(auth: Authenticated, body: ChallengeAccept.Body): Future[_] = {
    val keys = Seq(auth.user.id, body.opponentId.toLong)
      .map(StreamingKey.Challenge.apply)
      .map(_.toString)
    Async.join(
      redisCli.del(keys: _*),
      redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}")
    ).map { case (count, _) =>
      println(s"removed $count challenges on $keys and accept")
    }
  }
}
