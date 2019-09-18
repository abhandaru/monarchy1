package monarchy.marshalling.game

import monarchy.game._
import monarchy.util.Bijection

case class EffectProxy(
  name: String,
  point: Option[Vec] = None,
  power: Option[Int] = None
)

object EffectProxyBijection extends Bijection[Effect, EffectProxy] {
  case class FormatException(in: EffectProxy) extends RuntimeException(s"Unable to invert $in")

  override def apply(effect: Effect): EffectProxy = effect match {
    case Attack(p, pwr) => EffectProxy("Attack", point = Some(p), power = Some(pwr))
    case HealAll(pwr) => EffectProxy("HealAll", power = Some(pwr))
    case GrowPlant(p) => EffectProxy("GrowPlant", point = Some(p))
    case Paralyze(p) => EffectProxy("Paralyze", point = Some(p))
  }

  override def invert(proxy: EffectProxy): Effect = {
    val r = Option(proxy).collect {
      case EffectProxy("Attack", Some(p), Some(pwr)) => Attack(p, pwr)
      case EffectProxy("HealAll", _, Some(pwr)) => HealAll(pwr)
      case EffectProxy("GrowPlant", Some(p), _) => GrowPlant(p)
      case EffectProxy("Paralyze", Some(p), _) => Paralyze(p)
    }
    r.getOrElse(throw FormatException(proxy))
  }
}
