package monarchy.streaming.process

import monarchy.dal
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.concurrent.{Future, ExecutionContext}

trait ClientActionProcessor[T <: StreamAction] extends (T => Future[Unit])

class ClientActionProxy(implicit
  ec: ExecutionContext,
  redisCli: RedisClient,
  queryCli: dal.QueryClient
) extends ClientActionProcessor[StreamAction] {
  // Cached processors
  val challengeSeek = new ChallengeSeekProcessor
  val challengeSeekCancel = new ChallengeSeekCancelProcessor
  val challengeAccept = new ChallengeAcceptProcessor
  val gameSelectTile = new GameSelectTileProcessor
  val gameDeselectTile = new GameDeselectTileProcessor

  override def apply(action: StreamAction): Future[Unit] = {
    action match {
      case axn: ChallengeSeek => challengeSeek(axn)
      case axn: ChallengeSeekCancel => challengeSeekCancel(axn)
      case axn: ChallengeAccept => challengeAccept(axn)
      case axn: GameSelectTile => gameSelectTile(axn)
      case axn: GameDeselectTile => gameDeselectTile(axn)
      case axn =>
        println(s"[client-action-proxy] unrecognized [[StreamAction]] of $axn")
        Async.Unit
    }
  }
}

class ChallengeSeekProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends ClientActionProcessor[ChallengeSeek] {
  override def apply(axn: ChallengeSeek): Future[Unit] = {
    val userId = axn.auth.userId
    Async.join(
      redisCli.set(StreamingKey.Challenge(userId), "true", exSeconds = Some(300)),
      redisCli.publish(StreamingChannel.Matchmaking, Json.stringify(Matchmaking(true)))
    ).map(_ => ())
  }
}

class ChallengeSeekCancelProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends ClientActionProcessor[ChallengeSeekCancel] {
  override def apply(axn: ChallengeSeekCancel): Future[Unit] = {
    val userId = axn.auth.userId
    Async.join(
      redisCli.del(StreamingKey.Challenge(userId)),
      redisCli.publish(StreamingChannel.Matchmaking, Json.stringify(Matchmaking(true)))
    ).map(_ => ())
  }
}
