package monarchy.game

sealed trait Change[+T]

case class Accept[+T](mut: T) extends Change[T]

sealed trait Reject extends Change[Nothing]

object Reject {
  case object CannotAttack extends Reject
  case object CannotChangeDirection extends Reject
  case object CannotDeselect extends Reject
  case object CannotMove extends Reject
  case object CannotSelect extends Reject
  case object ChangeOutOfTurn extends Reject
}

// case class
