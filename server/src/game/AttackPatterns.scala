package monarchy.game

case class AttackPattern(deltas: Deltas) {
  def points(p0: Vec): Set[Vec] = deltas.map(_ + p0)
}

trait AttackPatterns {
  def patterns: Set[AttackPattern]
  def pointSets(p0: Vec): Set[Set[Vec]] = patterns.map(_.points(p0))
}

case class SimpleAttackPatterns(deltas: Deltas) extends AttackPatterns {
  override val patterns = deltas.map { d =>
    AttackPattern(Set(d))
  }
}

trait Effect

case class Attack(point: Vec, power: Int) extends Effect
case class GrowPlant(point: Vec) extends Effect
case class HealAll(power: Int) extends Effect

trait EffectArea {
  def effects(p0: Vec, ap: AttackPattern): Set[Effect]
}

case class UniformAttackArea(deltas: Set[Vec], power: Int) extends EffectArea {
  def effects(p0: Vec, ap: AttackPattern) = {
    for {
      p <- ap.points(p0)
      d <- deltas
    } yield Attack(p + d, power)
  }
}

object DeltaGenerator {
  val NoDelta = Set(Vec(0, 0))
  val AdjecentDeltas = Set(Vec(-1, 0), Vec(0, 1), Vec(1, 0), Vec(0, -1))

  def rangeDeltas(max: Int): Deltas = {
    val deltas = for {
      r <- -max to max
      maxCol = max - Math.abs(r)
      c <- -maxCol to maxCol
    } yield Vec(r, c)
    deltas.toSet
  }

  def rangeDeltasNoCenter(max: Int): Deltas = {
    rangeDeltas(max) - Vec(0, 0)
  }
}
