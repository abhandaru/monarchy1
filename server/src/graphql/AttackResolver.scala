package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.game._
import monarchy.streaming.core._

object AttackResolver extends Resolver[Unit, Selection] with StrictLogging {
  override def apply(in: In): Out = {
    val args = in.arg(Args.Attack)
    val commit = PhaseCommit(
      input = in,
      gameId = UUID.fromString(args.gameId),
      event = ctx => GameAttack(ctx.gameId),
      channel = ctx => StreamingChannel.gameAttack(ctx.userId)
    )
    commit { ctx =>
      val attack = args.attack.map(extractVec).toSet
      ctx.game.attackSelect(PlayerId(ctx.userId), attack)
    }
  }

  private def extractVec(argVec: VecQuery): Vec =
    Vec(argVec.i, argVec.j)
}
