package monarchy.marshalling

import monarchy.game._

object GameJsonBijection extends JsonBijection[Game] {
  override def invert(json: String): Game = ???
  override def apply(game: Game): String = {
    ???
  }
}

