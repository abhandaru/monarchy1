package monarchy.game

sealed trait Change[+T] {
  def map[U](f: T => U): Change[U]
  def flatMap[U](f: T => Change[U]): Change[U]
  def accepted: Boolean
  def rejected: Boolean
}

case class Accept[+T](mut: T) extends Change[T] {
  override def map[U](f: T => U) = Accept(f(mut))
  override def flatMap[U](f: T => Change[U]) = f(mut)
  override def accepted = true
  override def rejected = false
}

object Accept {
  val Unit: Accept[Unit] = Accept(())
}

sealed trait Reject extends Change[Nothing] {
  override def map[U](f: Nothing => U) = this
  override def flatMap[U](f: Nothing => Change[U]) = this
  override def accepted = false
  override def rejected = true
}

object Reject {
  case object ReadOnly extends Reject
  case object CannotAttack extends Reject
  case object CannotChangeDirection extends Reject
  case object CannotDeselect extends Reject
  case object CannotMove extends Reject
  case object CannotSelect extends Reject
  case object ChangeOutOfTurn extends Reject
  case object IllegalAttackSelection extends Reject
  case object IllegalDirSelection extends Reject
  case object IllegalMoveSelection extends Reject
  case object PieceActionWithoutSelection extends Reject
  case object PieceActionWithoutOwnership extends Reject
}
