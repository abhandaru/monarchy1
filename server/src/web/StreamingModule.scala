package monarchy.web

import monarchy.streaming._
import monarchy.dal.QueryClient
import redis.RedisClient
import scala.concurrent.ExecutionContext

object StreamingModule {
  def streamActionRenderer(implicit
    ec: ExecutionContext,
    queryCli: QueryClient,
    redisCli: RedisClient
  ): StreamActionRenderer = {
    // Just cache these.
    val matchmaking = new MatchmakingRenderer
    val pong = new PongRenderer
    val redisRaw = new RedisRawRenderer

    // Give parent renderer a mapping spec.
    StreamActionRenderer({
      case axn: Matchmaking => matchmaking(axn)
      case axn: Pong => pong(axn)
      case axn: RedisRaw => redisRaw(axn)
    })
  }
}
