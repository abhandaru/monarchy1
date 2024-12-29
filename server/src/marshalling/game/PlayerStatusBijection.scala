package monarchy.marshalling.game

import monarchy.game._
import monarchy.util.StringBijection

object PlayerStatusBijection extends StringBijection[Player.Status] {
  override def apply(status: Player.Status): String =
    status.toString
  
  override def invert(text: String): Player.Status = {
    text match {
      case "Playing" => Player.Status.Playing
      case "Won" => Player.Status.Won
      case "Lost" => Player.Status.Lost
      case "Drawn" => Player.Status.Drawn
      case _ => Player.Status.Invalid
    }
  }
}
