package monarchy.graphql

import monarchy.dal.QueryClient
import scala.concurrent.ExecutionContext

case class GraphqlContext(implicit
  executionContext: ExecutionContext,
  queryCli: QueryClient
)
