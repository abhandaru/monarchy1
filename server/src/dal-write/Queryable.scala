package monarchy.dalwrite

import monarchy.dal
import monarchy.dal.PostgresProfile.Implicits._
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Could not find an [[Queryable]] for ${E} in Queryable.scala")
trait Queryable[E] {
  type TableType <: Table[E]
  def query: TableQuery[TableType]
  def id: E => Long
  def repId: TableType => Rep[Long]
}

object Queryable {
  class Simple[E, T <: dal.TableDef[E]](
    override val query: TableQuery[T],
    override val id: E => Long
  ) extends Queryable[E] {
    override type TableType = T
    override val repId = _.id
  }

  implicit object User extends Simple[dal.User, dal.UserTable](dal.User.query, _.id)
  implicit object Game extends Simple[dal.Game, dal.GameTable](dal.Game.query, _.id)
  implicit object Player extends Simple[dal.Player, dal.PlayerTable](dal.Player.query, _.id)
}
