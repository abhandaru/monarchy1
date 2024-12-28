package monarchy.dal

import com.github.tminglei.slickpg._

/**
 * See README.md here for more extensions:
 * https://github.com/tminglei/slick-pg
 */
trait PostgresProfile extends ExPostgresProfile with PgDate2Support {
  override val api = Implicits
  object Implicits extends ExtPostgresAPI
}

object PostgresProfile extends PostgresProfile
