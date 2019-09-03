package monarchy.dalwrite

import monarchy.dal
import monarchy.postgres.PostgresProfile.Implicits._
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Could not find an [[Queryable]] for ${E} in Queryable.scala")
trait Queryable[E] {
  type TableType <: Table[E]
  def query: TableQuery[TableType]
  def id: E => Long
  def repId: TableType => Rep[Long]
}

class QueryableEntity[E <: dal.Entity, T <: dal.EntityTable[E]](
  override val query: TableQuery[T],
) extends SchemaConf[E] {
  override type TableType = T
  override val id = _.id
  override val repId = _.id
}

object Queryable {
  implicit object User extends QueryableEntity(dal.User.query)
  implicit object Game extends QueryableEntity(dal.Game.query)
  implicit object Player extends QueryableEntity(dal.Player.query)
}
