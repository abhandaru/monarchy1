package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.Json
import sangria.schema.Context
import scala.concurrent.Future

case class PhaseCommit(
    input: PhaseCommit.Input,
    gameId: UUID,
    event: PhaseCommit.CommitContext => StreamAction,
    channel: PhaseCommit.CommitContext => String
) extends StrictLogging { 
  import GameStringDeserializer._
  import PhaseCommit._

  def apply(commit: CommitContext => Change[Game]): Future[Selection] = {
    import input.ctx._
    val userId = Resolver.expectUserId(input)
    val gameKey = StreamingKey.Game(gameId)
    redisCli.get[Game](gameKey).flatMap {
      case None => Future.failed(NotFound(s"game '$gameId' not found"))
      case Some(game) =>
        val commitContext = CommitContext(userId, gameId, gameKey, game)
        commit(commitContext) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
              case true =>
                val evt = event(commitContext)
                val ch = channel(commitContext)
                logger.info(s"publishing event=$evt to channel=$ch")
                redisCli.publish(ch, Json.stringify(evt))
                  .map(_ => Selection(nextGame))
            }
        }
    }
  }
}

object PhaseCommit {
  type Input = Context[GraphqlContext, Unit]
  case class CommitContext(
      userId: UUID,
      gameId: UUID,
      gameKey: StreamingKey.Game,
      game: Game
  )
}