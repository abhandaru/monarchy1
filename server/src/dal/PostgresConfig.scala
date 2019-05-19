package monarchy.dal

import com.typesafe.config.Config
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend.Database

case class PostgresConfig(
  url: String,
  user: String = ""
) extends DatabaseConfig[JdbcProfile] {
  override val config: Config = None.orNull // unused
  override val profileIsObject: Boolean = true
  override val profileName: String = "org.postgresql.Driver"
  override val profile: JdbcProfile = PostgresProfile
  override val driver: JdbcProfile = PostgresProfile

  // Actual database connection
  override val db: JdbcProfile#Backend#Database = {
    Database.forURL(url, user, password = "", driver = profileName)
  }
}
