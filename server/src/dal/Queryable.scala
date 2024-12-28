package monarchy.dal

import java.util.UUID
import scala.annotation.implicitNotFound
import PostgresProfile.Implicits._

@implicitNotFound(msg = "Could not find an [[Queryable]] for ${E} in Queryable.scala")
trait Queryable[E] {
  type TableType <: Table[E]
  def query: TableQuery[TableType]
  def id: E => UUID
  def repId: TableType => Rep[UUID]
}

object Queryable {
  class Simple[E, T <: TableDef[E]](
    override val query: TableQuery[T],
    override val id: E => UUID
  ) extends Queryable[E] {
    override type TableType = T
    override val repId = _.id
  }
}
