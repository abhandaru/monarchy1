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
    val playersReq = queryCli.all(dal.Player.query.filter(_.gameId === gameId))

    Async.join(gameReq, playersReq).flatMap {
      case (None, _) => Future.failed(NotFound(s"game '$gameId' not found"))
      case (Some(game), players) =>
        val playerId = actingPlayerId(userId, players)
        val commitContext = CommitContext(userId, playerId, gameId, gameKey, game)
        commit(commitContext) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
              case true =>
                publish(commitContext, players).map(_ => Selection(nextGame))
            }
        }
    }
  }

  private def publish(ctx: CommitContext, players: Seq[dal.Player]): Future[Int] = {
    import input.ctx._
    val evt = event(ctx)
    val channels = players.map { p => StreamingChannel.gameChange(p.userId) }
    val fanout = channels.map(ch => redisCli.publish(ch, Json.stringify(evt)))
    Future.sequence(fanout).map(_.sum.toInt)
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

  private def actingPlayerId(userId: UUID, players: Seq[dal.Player]): PlayerId = {
    players.find(_.userId == userId) match {
      case Some(player) => PlayerId(player.id)
      case None => throw new RuntimeException(s"user '$userId' not participating")
    }
  }
}
