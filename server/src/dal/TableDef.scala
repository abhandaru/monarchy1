package monarchy.dal

import java.time.Instant
import PostgresProfile.Implicits._

abstract class TableDef[E](
  tag: Tag,
  tableName: String,
) extends Table[E](tag, None, tableName) {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def createdAt = column[Instant]("created_at", O.AutoInc)
  def updatedAt = column[Instant]("updated_at", O.AutoInc)
}
