package monarchy.web

import monarchy.streaming.core._
import monarchy.streaming.format._
import monarchy.streaming.process.ClientActionProxy
import monarchy.dal.QueryClient
import redis.RedisClient
import scala.concurrent.ExecutionContext

object StreamingModule {
  def clientActionProxy(implicit
    ec: ExecutionContext,
    queryCli: QueryClient,
    redisCli: RedisClient
  ): ClientActionProxy = new ClientActionProxy

  def streamActionRenderer(implicit
    ec: ExecutionContext,
    queryCli: QueryClient,
    redisCli: RedisClient
  ): StreamActionRenderer = {
    // Just cache these.
    val gameCreate = new GameCreateRenderer
    val matchmaking = new MatchmakingRenderer
    val pong = new PongRenderer
    val redisRaw = new RedisRawRenderer

    // Give parent renderer a mapping spec.
    StreamActionRenderer({
      case axn: Matchmaking => matchmaking(axn)
      case axn: GameCreate => gameCreate(axn)
      case axn: Pong => pong(axn)
      case axn: RedisRaw => redisRaw(axn)
    })
  }
}
