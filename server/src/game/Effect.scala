package monarchy.game

/**
 * An [[Effect]] represents an influence on the [[Board]].
 * The logic to apply an effect to the board is arbitrary and handled by the
 * game engine.
 */
sealed trait Effect
sealed trait LocalEffect extends Effect {
  def point: Vec
}

sealed trait FocusBreaking

case class Attack(point: Vec, power: Int) extends LocalEffect with FocusBreaking
case class GrowPlant(point: Vec) extends LocalEffect
case class HealAll(power: Int) extends Effect
case class Paralyze(point: Vec) extends LocalEffect with FocusBreaking

/**
 * NOTE: An [[Ordering]] is provided for deterministic order of execution when
 *   processing a collection of [[Effect]]s. This way the random number
 *   generator can reproduce probabilistic outcomes when turns are replayed.
 *
 * Ensure a collection of Effects has a universal ordering. Make sure updates
 * to add new effects are appended so older implementations never move in the
 * ordering when re-sorted.
 */
object Effect {
  // Return some ordered tuple. Using (Int, Int, Int, Int) for now.
  implicit val Ord: Ordering[Effect] = Ordering.by {
    case Attack(p, pwr) => (0, p.i, p.j, pwr)
    case HealAll(_) =>     (1, 0, 0, 0)
    case GrowPlant(p) =>   (2, p.i, p.j, 0)
    case Paralyze(p) =>    (3, p.i, p.j, 0)
  }
}

trait EffectArea {
  def apply(p0: Vec, pat: PointPattern): Set[Effect]
}

case object NullEffectArea extends EffectArea {
  override def apply(p0: Vec, pat: PointPattern) = Set.empty
}

case class UniformAttackArea(deltas: Set[Vec], power: Int) extends EffectArea {
  override def apply(p0: Vec, pat: PointPattern) = {
    for {
      p <- pat(p0)
      d <- deltas
    } yield Attack(p + d, power)
  }
}
