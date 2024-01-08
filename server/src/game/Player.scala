package monarchy.game

import java.util.UUID

case class PlayerId(id: UUID) extends AnyVal

object Player {
  type Formation = Seq[(Vec, PieceConf)]
}

case class Player(
  id: PlayerId,
  formation: Player.Formation
)
