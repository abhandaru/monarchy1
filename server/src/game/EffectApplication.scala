package monarchy.game

case class EffectLocation(point: Vec, effect: Effect)

object EffectLocation {
  implicit val Ord: Ordering[EffectLocation] = Ordering.by(_.effect)

  def apply(board: Board, piece: Piece, effect: Effect): Seq[EffectLocation] = {
    effect match {
      case e @ Attack(p, _) => Seq(EffectLocation(p, e))
      case e @ GrowPlant(p) => Seq(EffectLocation(p, e))
      case e @ HealAll(_) =>
        board.pieces(piece.playerId).map {
          case PieceLocation(p, _) => EffectLocation(p, e)
        }
    }
  }
}
