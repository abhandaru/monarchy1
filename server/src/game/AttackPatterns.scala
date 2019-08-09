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
