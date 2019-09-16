package monarchy.graphql

import monarchy.dal.QueryClient
import scala.concurrent.ExecutionContext
import redis.RedisClient

class GraphqlContext(implicit
  val executionContext: ExecutionContext,
  val queryCli: QueryClient,
  val redisCli: RedisClient
)
