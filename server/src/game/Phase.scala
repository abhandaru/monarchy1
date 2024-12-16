package monarchy.game

sealed trait Phase

object Phase {
  case object Move extends Phase
  case object Attack extends Phase
  case object Dir extends Phase
  case object End extends Phase

  def values: Set[Phase] =
    Set(Move, Attack, Dir, End)
}
