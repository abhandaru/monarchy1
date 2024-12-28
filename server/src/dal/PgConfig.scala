package monarchy.dal

import com.typesafe.config.Config
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.util.AsyncExecutor

case class PgConfig(
    jdbcUrl: String,
    threads: Int,
) extends DatabaseConfig[JdbcProfile] {
  import PgConfig._
  import profile.backend.Database

  override val config: Config = None.orNull // unused
  override val profileIsObject: Boolean = true
  override val profileName: String = "org.postgresql.Driver"
  override val profile: JdbcProfile = PostgresProfile

  // Actual database connection
  override val db: Database = {
    // see: https://github.com/slick/slick/blob/master/slick/src/main/scala/slick/util/AsyncExecutor.scala
    val asyncExecutor = AsyncExecutor(
      name = s"SlickAsyncExecutor",
      minThreads = threads,
      maxThreads = threads,
      maxConnections = threads,
      queueSize = QueueMultiplier * threads,
      registerMbeans = true,
    )
    Database.forDataSource(
      ds = PgConnection(jdbcUrl, profileName, poolSize = threads),
      maxConnections = None,
      keepAliveConnection = true,
      executor = asyncExecutor,
    )
  }
}

object PgConfig {
  val QueueMultiplier = 5
}

