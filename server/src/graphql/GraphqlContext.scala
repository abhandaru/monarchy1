package monarchy.graphql

import monarchy.dal.QueryClient

case class GraphqlContext(implicit
  queryCli: QueryClient
)
