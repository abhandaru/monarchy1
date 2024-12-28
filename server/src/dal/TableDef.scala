package monarchy.dal

import java.time.Instant
import java.util.UUID
import PostgresProfile.Implicits._

abstract class TableDef[E](
  tag: Tag,
  tableName: String,
) extends Table[E](tag, None, tableName) {
  def id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
  def createdAt = column[Instant]("created_at", O.AutoInc)
  def updatedAt = column[Instant]("updated_at", O.AutoInc)
}

class TableSchema[
    E <: TableSchema.HasId,
    TT <: TableDef[E]
](val query: TableQuery[TT]) {
  implicit val queryable: Queryable[E] =
    new Queryable.Simple[E, TT](query, _.id)
}

object TableSchema {
  type HasId = { def id: UUID }
}