package monarchy.game

import java.util.UUID

case class PlayerId(id: UUID) extends AnyVal

object Player {
  type Formation = Seq[(Vec, PieceConf)]

  sealed trait Status
  object Status {
    case object Playing extends Status
    case object Won extends  Status
    case object Lost extends Status
    case object Drawn extends Status
    case object Invalid extends Status
  }
}

case class Player(
    id: PlayerId,
    formation: Player.Formation,
    status: Player.Status = Player.Status.Playing,
)
