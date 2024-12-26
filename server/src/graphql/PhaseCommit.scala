package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.dal
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import sangria.schema.Context
import scala.concurrent.Future

case class PhaseCommit(
    input: PhaseCommit.Input,
    gameId: UUID,
    event: PhaseCommit.CommitContext => StreamAction
) extends StrictLogging { 
  import dal.PostgresProfile.Implicits._
  import GameStringDeserializer._
  import PhaseCommit._

  def apply(commit: CommitContext => Change[Game]): Future[Selection] = {
    import input.ctx._
    val userId = Resolver.expectUserId(input)
    val gameKey = StreamingKey.Game(gameId)
    val gameReq = redisCli.get[Game](gameKey)
    val playerReq = queryCli.first(
      dal.Player.query
        .filter(_.gameId === gameId)
        .filter(_.userId === userId)
    )

    Async.join(gameReq, playerReq).flatMap {
      // Game not found
      case (None, _) => Future.failed(NotFound(s"game '$gameId' not found"))
      // User not found in game
      case (_, None) => Future.failed(NotFound(s"user '$userId' not found in game '$gameId'"))
      // Happy path
      case (Some(game), Some(player)) =>
        val playerId = PlayerId(player.id)
        val commitContext = CommitContext(userId, playerId, gameId, gameKey, game)
        commit(commitContext) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
              case true =>
                val evt = event(commitContext)
                val ch = StreamingChannel.gameChange(commitContext.userId)
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
      playerId: PlayerId,
      gameId: UUID,
      gameKey: StreamingKey.Game,
      game: Game
  )
}
