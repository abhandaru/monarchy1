package monarchy.dal

import com.github.tminglei.slickpg._

/**
 * See README.md here for more extensions:
 * https://github.com/tminglei/slick-pg
 */
trait PostgresProfile extends ExPostgresProfile {
  override val api = PostgresApi
  object PostgresApi extends API
}

object PostgresProfile extends PostgresProfile
