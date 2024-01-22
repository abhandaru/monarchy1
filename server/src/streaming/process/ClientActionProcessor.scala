package monarchy.streaming.process

import monarchy.dal
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.concurrent.{Future, ExecutionContext}

trait ClientActionProcessor[T <: StreamAction] extends (T => Future[StreamAction])

class ClientActionProxy(implicit
  ec: ExecutionContext,
  redisCli: RedisClient,
  queryCli: dal.QueryClient
) extends ClientActionProcessor[StreamAction] {
  // Cached processors
  val ping = new PingProcessor
  val challengeSeekCancel = new ChallengeSeekCancelProcessor
  val challengeAccept = new ChallengeAcceptProcessor

  override def apply(action: StreamAction): Future[StreamAction] = {
    action match {
      case axn: Ping => ping(axn)
      case axn: ChallengeSeekCancel => challengeSeekCancel(axn)
      case axn: ChallengeAccept => challengeAccept(axn)
      case axn =>
        println(s"[client-action-proxy] unrecognized [[StreamAction]] of $axn")
        Async(StreamAction.Null)
    }
  }
}

class ChallengeSeekCancelProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends ClientActionProcessor[ChallengeSeekCancel] {
  override def apply(axn: ChallengeSeekCancel): Future[StreamAction] = {
    val userId = axn.auth.userId
    Async.join(
      redisCli.del(StreamingKey.Challenge(userId)),
      redisCli.publish(StreamingChannel.Matchmaking, Json.stringify(Matchmaking(true)))
    ).map(_ => StreamAction.Null)
  }
}
