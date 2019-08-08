package monarchy.game

sealed trait TurnAction
case class TileSelect(p: Vec) extends TurnAction
case object TileDeselect extends TurnAction
case class MoveSelect(p: Vec) extends TurnAction
case class AttackSelect(pat: Deltas) extends TurnAction
case class DirSelect(dir: Vec) extends TurnAction

case class Turn(
  actions: Seq[TurnAction] = Nil
) {
  import Reject._

  def act(action: TurnAction): Change[Turn] = {
    val error = Option(action).collect {
      case TileSelect(p) if !canSelect => CannotSelect
      case TileDeselect if !canDeselect => CannotDeselect
      case MoveSelect(p) if !canMove => CannotMove
      case AttackSelect(pat) if !canAttack => CannotAttack
      case DirSelect(dir) if !canDir => CannotChangeDirection
    }
    error match {
      case None => Accept(this.copy(actions = actions :+ action))
      case Some(reject) => reject
    }
  }

  // Action extractors
  def select: Option[Vec] =
    actions.collectFirst { case TileSelect(p) => p }

  def move: Option[Vec] =
    actions.collectFirst { case MoveSelect(p) => p }

  def attack: Option[Deltas] =
    actions.collectFirst { case AttackSelect(pat) => pat }

  def dir: Option[Vec] =
    actions.collectFirst { case DirSelect(dir) => dir }

  // State transition checks
  def canSelect: Boolean =
    move.isEmpty && attack.isEmpty && dir.isEmpty

  def canDeselect: Boolean =
    move.isEmpty && attack.isEmpty && dir.isEmpty

  def canMove: Boolean =
    select.nonEmpty && move.isEmpty && dir.isEmpty

  def canAttack: Boolean =
    select.nonEmpty && attack.isEmpty && dir.isEmpty

  def canDir: Boolean =
    dir.isEmpty
}
