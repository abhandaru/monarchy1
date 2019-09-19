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
  val gameChangeSelection = new GameChangeSelectionRenderer
  val gameCreate = new GameCreateRenderer
  val matchmaking = new MatchmakingRenderer
  val pong = new PongRenderer
  val redisRaw = new RedisRawRenderer

  override def apply(action: StreamAction): Future[Option[String]] = {
    action match {
      case axn: GameChangeSelection => gameChangeSelection(axn)
      case axn: GameCreate => gameCreate(axn)
      case axn: Matchmaking => matchmaking(axn)
      case axn: Pong => pong(axn)
      case axn: RedisRaw => redisRaw(axn)
      case axn =>
        println(s"[action-renderer-proxy] unrecognized [[StreamAction]] of $axn")
        Async.None
    }
  }
}
