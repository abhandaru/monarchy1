package monarchy.game

sealed trait Change[+T] {
  def map[U](f: T => U): Change[U]
  def flatMap[U](f: T => Change[U]): Change[U]
}

case class Accept[+T](mut: T) extends Change[T] {
  override def map[U](f: T => U) = Accept(f(mut))
  override def flatMap[U](f: T => Change[U]) = f(mut)
}

object Accept {
  val Unit: Accept[Unit] = Accept(())
}

sealed trait Reject extends Change[Nothing] {
  override def map[U](f: Nothing => U) = this
  override def flatMap[U](f: Nothing => Change[U]) = this
}

object Reject {
  case object CannotAttack extends Reject
  case object CannotChangeDirection extends Reject
  case object CannotDeselect extends Reject
  case object CannotMove extends Reject
  case object CannotSelect extends Reject
  case object ChangeOutOfTurn extends Reject
  case object DirIllegal extends Reject
  case object MoveIllegal extends Reject
  case object AttackIllegal extends Reject
  case object PieceActionWithoutSelection extends Reject
}
