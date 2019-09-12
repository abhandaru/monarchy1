package monarchy.game

case class PlayerId(id: Long) extends AnyVal

object Player {
  implicit val Ord: Ordering[Player] = Ordering.by(_.id.id)
  type Formation = Seq[(Vec, PieceConf)]
}

case class Player(
  id: PlayerId,
  formation: Player.Formation
)
