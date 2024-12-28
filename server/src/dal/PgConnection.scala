package monarchy.dal

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import scala.concurrent.duration._

case class PgConnection(
    jdbcUrl: String,
    driverName: String,
    poolSize: Int,
) extends HikariDataSource(new PgConnection.Conf(jdbcUrl, driverName, poolSize))

object PgConnection {
  class Conf(
      jdbcUrl: String,
      driverName: String,
      poolSize: Int,
  ) extends HikariConfig {
    // Essential connection parameters
    setDriverClassName(driverName)
    setJdbcUrl(jdbcUrl)

    // timeouts
    setConnectionTimeout(5.seconds.toMillis)
    setValidationTimeout(2.seconds.toMillis) // must be less than connectionTimeout
    setInitializationFailTimeout(1L) // this is on top of connectionTimeout
    setMaxLifetime(30.minute.toMillis)
    setIdleTimeout(10.minute.toMillis)

    // pool sizing
    setMaximumPoolSize(poolSize * 2)
    setMinimumIdle(poolSize / 2)

    // pool name
    setPoolName("pg-connections")

    // connection init
    setConnectionInitSql("SELECT 1;")
    setReadOnly(false)
    setLeakDetectionThreshold(0)
    setTransactionIsolation("TRANSACTION_READ_COMMITTED")
  }
}
