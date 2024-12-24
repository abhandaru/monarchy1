package monarchy.game

sealed trait TurnAction

object TurnAction {
  case class TileSelect(p: Vec) extends TurnAction
  case object TileDeselect extends TurnAction
  case class MoveSelect(p: Vec) extends TurnAction
  case class AttackSelect(pat: Deltas) extends TurnAction
  case class DirSelect(dir: Vec) extends TurnAction
  case object EndTurn extends TurnAction
}

case class Turn(actionStack: Seq[TurnAction] = Nil) {
  import Reject._
  import Turn._
  import TurnAction._

  def act(action: TurnAction): Change[Turn] = {
    val error = Option(action).collect {
      case TileSelect(p) if !canSelect => CannotSelect
      case TileDeselect if !canDeselect => CannotDeselect
      case MoveSelect(p) if !canMove => CannotMove
      case AttackSelect(pat) if !canAttack => CannotAttack
      case DirSelect(dir) if !canDir => CannotChangeDirection
      case EndTurn if !canEnd => CannotEndTurn
    }
    error match {
      case Some(reject) => reject
      case None =>
        val nextStack = incrementallyConsolidate(actionStack, action)
        Accept(this.copy(actionStack = nextStack))
    }
  }

  // Action extractors
  def select: Option[Vec] =
    actionStack.collectFirst { case TileSelect(p) => p }

  def move: Option[Vec] =
    actionStack.collectFirst { case MoveSelect(p) => p }

  def attack: Option[Deltas] =
    actionStack.collectFirst { case AttackSelect(pat) => pat }

  def dir: Option[Vec] =
    actionStack.collectFirst { case DirSelect(dir) => dir }


  // State transition checks
  def canSelect: Boolean =
    canEnd && move.isEmpty && attack.isEmpty && dir.isEmpty

  def canDeselect: Boolean =
    canEnd && move.isEmpty && attack.isEmpty && dir.isEmpty

  def canMove: Boolean =
    canEnd && select.nonEmpty && move.isEmpty && dir.isEmpty

  def canAttack: Boolean =
    canEnd && select.nonEmpty && attack.isEmpty && dir.isEmpty

  def canDir: Boolean =
    canEnd && select.nonEmpty && dir.isEmpty

  def canEnd: Boolean =
    !actionStack.contains(TurnAction.EndTurn)

  def actions: Seq[TurnAction] =
    actionStack.reverse

  // Ordered by suggested next phase
  def phases: Seq[Phase] = {
    Seq(
      if (canSelect && select.isEmpty) Some(Phase.Select) else None,
      if (canMove) Some(Phase.Move) else None,
      if (canAttack) Some(Phase.Attack) else None,
      if (canDir) Some(Phase.Dir) else None,
      if (canEnd) Some(Phase.End) else None,
    ).flatten
  }
}

object Turn {
  import TurnAction._

  val TileSelectMatcher: TurnAction => Boolean = {
    case _: TileSelect => true
    case _ => false
  }

  def incrementallyConsolidate(stack: Seq[TurnAction], action: TurnAction): Seq[TurnAction] = {
    action match {
      case TileDeselect => stack.filterNot(TileSelectMatcher)
      case axn @ TileSelect(_) => axn +: stack.filterNot(TileSelectMatcher)
      case axn => axn +: stack
    }
  }
}
