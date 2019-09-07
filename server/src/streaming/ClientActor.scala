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
      redisCli.set(StreamingKey.Challenge(auth.userId), "true", exSeconds = Some(300)),
      redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}")
    )
  }

  def challengeSeekCancel(auth: Authenticated): Future[_] = {
    Async.join(
      redisCli.del(StreamingKey.Challenge(auth.userId)),
      redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}")
    )
  }

  /**
   * TODO (adu): Actually solve this with some sort of mutex implementation
   * Partial impl: https://gist.github.com/abhandaru/712d4f2bfd360fe29b7ecefa81ac9c3f
   *
   * The async read/presence check then write below should be fast enough to avoid
   * most issues. We can move to something more correct as we bring on traffic.
   */
  def challengeAccept(auth: Authenticated, body: ChallengeAccept.Body): Future[_] = {
    val userId = auth.userId
    val opponentUserId = body.opponentId.toLong
    if (userId == opponentUserId) {
      Async.Unit
    } else {
      val challengeKey = StreamingKey.Challenge(userId).toString
      val opponentChallengeKey = StreamingKey.Challenge(opponentUserId).toString
      redisCli.get[Boolean](opponentChallengeKey).flatMap {
        case None => Async.Unit
        case Some(false) => Async.Unit
        case Some(true) =>
          Async.join(
            redisCli.del(challengeKey, opponentChallengeKey),
            redisCli.publish(StreamingChannel.Matchmaking, "{\"check\":true}"),
            redisCli.publish(StreamingChannel.personal(userId), "{\"gameId\":77}"),
            redisCli.publish(StreamingChannel.personal(opponentUserId), "{\"gameId\":77}")
          ).map { case (count, _,  _, _) =>
            println(s"removed $count challenges on $challengeKey, $opponentChallengeKey and accept")
          }
      }
    }
  }
}
