package monarchy.web

import monarchy.dal.QueryClient
import monarchy.graphql.GraphqlContext

object GraphqlModule {
  implicit def graphqlContext(implicit queryCli: QueryClient) = new GraphqlContext
}
