package monarchy.graphql

import monarchy.auth.{Auth, NullAuth}
import monarchy.dal.QueryClient
import redis.RedisClient
import scala.concurrent.ExecutionContext

class GraphqlContext(
    val auth: Auth = NullAuth
)(
    implicit
    val executionContext: ExecutionContext,
    val queryCli: QueryClient,
    val redisCli: RedisClient
) {
  def withAuth(_auth: Auth): GraphqlContext =
    new GraphqlContext(_auth)
}
