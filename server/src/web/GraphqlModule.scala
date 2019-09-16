package monarchy.web

import monarchy.dal.QueryClient
import monarchy.graphql.GraphqlContext
import scala.concurrent.ExecutionContext
import redis.RedisClient

object GraphqlModule {
  implicit def graphqlContext(implicit
    redisCli: RedisClient,
    queryCli: QueryClient,
    ec: ExecutionContext
  ): GraphqlContext = new GraphqlContext
}
