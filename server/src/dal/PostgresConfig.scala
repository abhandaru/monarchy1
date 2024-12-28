package monarchy.dal

import com.typesafe.config.Config
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

case class PostgresConfig(
  url: String,
  user: String = ""
) extends DatabaseConfig[JdbcProfile] {
  import profile.backend.Database

  override val config: Config = None.orNull // unused
  override val profileIsObject: Boolean = true
  override val profileName: String = "org.postgresql.Driver"
  override val profile: JdbcProfile = PostgresProfile

  // Actual database connection
  override val db: Database = {
    Database.forURL(url, user, password = "", driver = profileName)
  }
}
