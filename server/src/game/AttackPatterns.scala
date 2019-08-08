package monarchy.game

object PointPattern {
  def infer(origin: Vec, deltas: Deltas): PointPattern = {
    PointPattern(deltas.map(_ - origin))
  }
}

case class PointPattern(deltas: Deltas) {
  def apply(p0: Vec): Set[Vec] = deltas.map(_ + p0)
}

trait AttackPatterns {
  def patterns: Set[PointPattern]
  def pointSets(p0: Vec): Set[Set[Vec]] = patterns.map(_(p0))
}

case class SimpleAttackPatterns(deltas: Deltas) extends AttackPatterns {
  override val patterns = deltas.map { d =>
    PointPattern(Set(d))
  }
}

trait Effect

case class Attack(point: Vec, power: Int) extends Effect
case class GrowPlant(point: Vec) extends Effect
case class HealAll(power: Int) extends Effect

trait EffectArea {
  def apply(p0: Vec, pat: PointPattern): Set[Effect]
}

case class UniformAttackArea(deltas: Set[Vec], power: Int) extends EffectArea {
  override def apply(p0: Vec, pat: PointPattern) = {
    for {
      p <- pat(p0)
      d <- deltas
    } yield Attack(p + d, power)
  }
}
