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
  ): ActionRendererProxy = new ActionRendererProxy
}
