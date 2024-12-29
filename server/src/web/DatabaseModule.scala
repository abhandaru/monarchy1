package monarchy.web

import monarchy.dal
import scala.concurrent.ExecutionContext

object DatabaseModule {
  private val DefaultThreads = 10

  // Grab the database url from the environment
  private val DefaultDatabaseUrl = "jdbc:postgresql://localhost:5432/monarchy_local"
  private val DatabaseUrl = sys.env.getOrElse("JDBC_DATABASE_URL", DefaultDatabaseUrl)

  def queryClient(implicit ec: ExecutionContext): dal.QueryClient = {
    val config = dal.PgConfig(DatabaseUrl, DefaultThreads)
    dal.QueryClientImpl(config)
  }
}
