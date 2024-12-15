package monarchy.streaming.format

import monarchy.dal.QueryClient
import monarchy.streaming.core._
import monarchy.util.Async
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}

class ActionRendererProxy(implicit
  ec: ExecutionContext,
  queryCli: QueryClient,
  redisCli: RedisClient
) extends ActionRenderer[StreamAction] {
  // Cache the renderers
  val gameCreate = new GameCreateRenderer
  val matchmaking = new MatchmakingRenderer
  val pong = new PongRenderer
  val redisRaw = new RedisRawRenderer

  override def apply(action: StreamAction): Future[Option[String]] = {
    action match {
      case axn: GameCreate => gameCreate(axn)
      case axn: Matchmaking => matchmaking(axn)
      case axn: Pong => pong(axn)
      case axn: RedisRaw => redisRaw(axn)
      // Just ignore these for now. Can forward to spectators later.
      case axn: GameChangeSelection => Async.None
      case axn =>
        println(s"[action-renderer-proxy] unrecognized [[StreamAction]] of $axn")
        Async.None
    }
  }
}
