package monarchy.game

case class EffectLocation(point: Vec, effect: Effect)

object EffectLocation {
  implicit val Ord: Ordering[EffectLocation] = Ordering.by(_.effect)

  def apply(board: Board, piece: Piece, effect: Effect): Seq[EffectLocation] = {
    effect match {
      case e @ Attack(p, _) => Seq(EffectLocation(p, e))
      case e @ GrowPlant(p) => Seq(EffectLocation(p, e))
      case e @ Paralyze(p) => Seq(EffectLocation(p, e))
      case e @ HealAll(_) =>
        board.pieces(piece.playerId).map {
          case PieceLocation(p, _) => EffectLocation(p, e)
        }
    }
  }
}

object EffectGeometry {
  val BoundaryTolerance = 1e-4 // radians
  val π = math.Pi

  sealed trait Snap {
    def result: Vec
  }

  case class Clockwise90(result: Vec) extends Snap
  case class CounterClockwise90(result: Vec) extends Snap
  case class NoRotation(result: Vec) extends Snap
  case class HalfRotation(result: Vec) extends Snap

  def directionSnap(v0: Vec, v: Vec): Snap = {
    val theta = v angle v0
    if (theta <= π/4 + BoundaryTolerance) {
      NoRotation(v0)
    } else if (theta >= 3*π/4 - BoundaryTolerance) {
      HalfRotation(-v0)
    } else if (v.curl(v0) > 0) {
      Clockwise90(-v0.perpendicular)
    } else {
      CounterClockwise90(v0.perpendicular)
    }
  }
}
