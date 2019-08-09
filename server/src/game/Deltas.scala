package monarchy.game

object Deltas {
  val Origin = Vec(0, 0)
  val NoDelta = Set(Origin)
  val AdjecentDeltas = Set(Vec(-1, 0), Vec(0, 1), Vec(1, 0), Vec(0, -1))

  def empty: Deltas = Set.empty[Vec]

  def rangeDeltas(max: Int): Deltas = {
    val deltas = for {
      r <- -max to max
      maxCol = max - math.abs(r)
      c <- -maxCol to maxCol
    } yield Vec(r, c)
    deltas.toSet
  }

  def rangeDeltasNoCenter(max: Int): Deltas = {
    rangeDeltas(max) - Vec(0, 0)
  }
}
