package monarchy.dalwrite

import scala.concurrent.ExecutionContext
import monarchy.dal.PostgresProfile

object WriteQueryBuilder {
  import PostgresProfile.Implicits._

  def put[E: SchemaConf](e: E)(implicit ec: ExecutionContext): DBIO[E] = {
    val schema = implicitly[SchemaConf[E]]
    val q = schema.query
    (q returning q).insertOrUpdate(e).map(_.getOrElse(e))
  }

  def putAll[E: SchemaConf](entities: Seq[E])(implicit ec: ExecutionContext): DBIO[Seq[E]] = {
    DBIO.sequence { entities.map(put(_)) }
  }
}
