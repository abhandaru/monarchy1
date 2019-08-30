package monarchy.web

import monarchy.dal.QueryClient
import monarchy.graphql.GraphqlContext
import scala.concurrent.ExecutionContext

object GraphqlModule {
  implicit def graphqlContext(implicit queryCli: QueryClient, ec: ExecutionContext) = {
    new GraphqlContext
  }
}
