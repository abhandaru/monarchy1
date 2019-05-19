package monarchy.web

import monarchy.dal
import scala.concurrent.ExecutionContext

object DatabaseModule {
  val DefaultDatabaseUrl = "jdbc:postgresql://localhost:5432/monarchy_local"
  val DatabaseUrl = sys.env.getOrElse("JDBC_DATABASE_URL", DefaultDatabaseUrl)
  val DatabaseUser = sys.env.getOrElse("USER", "")
  val PostgresConfig = dal.PostgresConfig(DatabaseUrl, DatabaseUser)

  def queryClient(implicit ec: ExecutionContext): dal.QueryClient = {
    dal.QueryClientImpl(PostgresConfig)
  }
}
