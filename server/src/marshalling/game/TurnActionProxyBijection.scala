package monarchy.marshalling.game

import monarchy.game._
import monarchy.util.Bijection

case class TurnActionProxy(
  name: String,
  p: Option[Vec] = None,
  pat: Option[Deltas] = None,
  dir: Option[Vec] = None
)

object TurnActionProxyBijection extends Bijection[TurnAction, TurnActionProxy] {
  import TurnAction._
  
  case class FormatException(in: TurnActionProxy) extends RuntimeException(s"Unable to invert $in")

  override def apply(axn: TurnAction) = axn match {
    case TileSelect(p) => TurnActionProxy("TileSelect", p = Some(p))
    case TileDeselect => TurnActionProxy("TileDeselect")
    case MoveSelect(p) => TurnActionProxy("MoveSelect", p = Some(p))
    case AttackSelect(pat) => TurnActionProxy("AttackSelect", pat = Some(pat))
    case DirSelect(dir) => TurnActionProxy("DirSelect", dir = Some(dir))
    case EndTurn => TurnActionProxy("EndTurn")
    case Forfeit => TurnActionProxy("Forfeit")
  }

  override def invert(proxy: TurnActionProxy) = {
    val result = Option(proxy).collect {
      case TurnActionProxy("TileSelect", Some(p), _, _) => TileSelect(p)
      case TurnActionProxy("TileDeselect", _, _, _) => TileDeselect
      case TurnActionProxy("MoveSelect", Some(p), _, _) => MoveSelect(p)
      case TurnActionProxy("AttackSelect", _, Some(pat), _) => AttackSelect(pat)
      case TurnActionProxy("DirSelect", _, _, Some(dir)) => DirSelect(dir)
      case TurnActionProxy("EndTurn", _, _, _) => EndTurn
      case TurnActionProxy("Forfeit", _, _, _) => Forfeit
    }
    result.getOrElse(throw FormatException(proxy))
  }
}

