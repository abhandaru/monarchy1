package monarchy.dalwrite

import monarchy.dal
import monarchy.dal.PostgresProfile.Implicits._
import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Could not find an [[Descendant]] for ${E} in Descendant.scala")
trait Descendant[E] {
  type TableType <: Table[E]
  def parentId: E => Long
  def liftedParentId: TableType => Rep[Long]
}

object Descendant {
  class Simple[E, T <: Table[E]](
    override val parentId: E => Long,
    override val liftedParentId: T => Rep[Long]
  ) extends Descendant[E] {
    type TableType = T
  }

  implicit object Player extends Simple[dal.Player, dal.PlayerTable](_.gameId, _.gameId)
}

