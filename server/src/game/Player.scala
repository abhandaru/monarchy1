package monarchy.game

case class PlayerId(id: Long) extends AnyVal

object Player {
  implicit val Ord: Ordering[Player] = Ordering.by(_.id.id)
}

case class Player(
  id: PlayerId,
  formation: Seq[(Vec, PieceConf)]
)
