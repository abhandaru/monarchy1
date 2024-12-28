package monarchy.graphql

import java.util.UUID
import monarchy.game._
import monarchy.streaming.core._

object MoveResolver extends Resolver[Unit, Selection] {
  override def apply(in: In): Out = {
    val args = in.arg(GqlArgs.Move)
    val commit = PhaseCommit(
      input = in,
      gameId = UUID.fromString(args.gameId),
      event = ctx => GameChange(ctx.gameId, "MOVE")
    )
    commit { ctx =>
      val point = Vec(args.point.i, args.point.j)
      ctx.game.moveSelect(ctx.playerId, point)
    }
  }
}
