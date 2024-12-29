package monarchy.marshalling.game

import monarchy.game._
import monarchy.util.StringBijection

object GameStatusBijection extends StringBijection[Game.Status] {
  override def apply(status: Game.Status): String =
    status.toString
  
  override def invert(text: String): Game.Status = {
    text match {
      case "Started" => Game.Status.Started
      case "Complete" => Game.Status.Complete
      case _ => Game.Status.Invalid
    }
  }
}
