package monarchy.game

case class PlayerId(val id: Long) extends AnyVal

object Player {
  implicit val Ord: Ordering[Player] = Ordering.by(_.id.id)
}

case class Player(
  id: PlayerId,
  formation: Seq[(Vec, PieceConf)]
)
