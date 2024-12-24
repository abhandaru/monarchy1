package monarchy.marshalling.game

import java.util.UUID
import monarchy.game._
import monarchy.util.StringBijection

object PlayerIdBijection extends StringBijection[PlayerId] {
  override def apply(id: PlayerId): String =
    id.id.toString
  
  override def invert(text: String): PlayerId =
    PlayerId(UUID.fromString(text))
}
