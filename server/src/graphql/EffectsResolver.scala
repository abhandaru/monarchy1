package monarchy.graphql

import java.util.UUID
import monarchy.game._
import monarchy.marshalling.game.GameStringDeserializer
import monarchy.streaming.core.StreamingKey
import scala.concurrent.Future

object EffectsResolver extends Resolver[Unit, Seq[EffectLocation]] {
  import GameStringDeserializer._

  override def apply(in: In): Out = {
    import in.ctx._
    val args = in.arg(Args.Attack)
    val gameId = UUID.fromString(args.gameId)
    redisCli.get[Game](StreamingKey.Game(gameId)).map {
      case None => throw NotFound(s"game '$gameId' not found")
      case Some(game) => game.effects(Set(extractPoint(args))).toSeq
    }
  }

  private def extractPoint(args: AttackQuery) =
    Vec(args.point.i, args.point.j)
}
