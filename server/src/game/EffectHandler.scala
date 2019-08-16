package monarchy.game

case class EffectLocation(point: Vec, effect: Effect)

object EffectLocation {
  implicit val Ord: Ordering[EffectLocation] = Ordering.by(_.effect)

  def apply(board: Board, pl: PieceLocation, effect: Effect): Seq[EffectLocation] = {
    val PieceLocation(p0, piece) = pl
    val els = effect match {
      case e: LocalEffect => Seq(EffectLocation(e.point, e))
      case e @ HealAll(_) =>
        board.pieces(piece.playerId).map {
          case PieceLocation(p, _) => EffectLocation(p, e)
        }
    }
    piece.conf.attackAlongLos match {
      case false => els
      case true => els.map { el =>
        val pointsInTheWay = EffectGeometry.pointsAlongSegment(p0, el.point)
        val redirect = pointsInTheWay.find(board.occupied).getOrElse(el.point)
        el.copy(point = redirect)
      }
    }
  }
}

// trait EffectHandler extends PartialFunction[EffectLocation, CanChangeBoard]
