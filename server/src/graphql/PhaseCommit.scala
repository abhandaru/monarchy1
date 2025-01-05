package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.dal
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import sangria.schema.Context
import scala.concurrent.Future

case class PhaseCommit(
    input: PhaseCommit.Input,
    gameId: UUID,
    event: PhaseCommit.CommitContext => StreamAction,
    continuation: PhaseCommit.CommitContext => Change[Game] = PhaseCommit.NoContinuation
) extends StrictLogging { 
  import dal.PostgresProfile.Implicits._
  import input.ctx._
  import GameStringDeserializer._
  import PhaseCommit._

  def apply(commit: CommitContext => Change[Game]): Future[Selection] = {
    val userId = Resolver.expectUserId(input)
    val gameKey = StreamingKey.Game(gameId)
    val gameReq = redisCli.get[Game](gameKey)
    val playersReq = queryCli.all(dal.Player.query.filter(_.gameId === gameId))

    Async.join(gameReq, playersReq).flatMap {
      case (None, _) => Future.failed(NotFound(s"game '$gameId' not found"))
      case (Some(game), players) =>
        val playerId = actingPlayerId(userId, players)
        val commitContext = CommitContext(userId, playerId, players, gameId, gameKey, game)
        commit(commitContext) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            val nextCommitContext = commitContext.copy(game = nextGame)
            Completion(nextGame) match {
              case Completion.Incomplete => commitContinue(nextCommitContext)
              case cmpl: Completion.Complete => commitComplete(nextCommitContext, cmpl)
            }
        }
    }
  }

  private def commitContinue(ctx: CommitContext): Future[Selection] = {
    continuation(ctx) match {
      case r: Reject => Future.failed(Rejection(r))
      case Accept(game) =>
        val nextCommitContext = ctx.copy(game = game)
        persistRedis(nextCommitContext)
    }
  }

  // If the REDIS update fails, do not update the postgres database. We can
  // always retry the phase commit using the previous redis state.
  private def commitComplete(ctx: CommitContext, cmpl: Completion.Complete): Future[Selection] = {
    val players = ctx.game.players.map { p =>
      val status = cmpl.playerStatuses.getOrElse(p.id, p.status)
      p.copy(status = status)
    }
    val game = ctx.game.copy(status = cmpl.gameStatus, players = players)
    val nextCommitContext = ctx.copy(game = game)
    for {
      sel <- persistRedis(nextCommitContext)
      _ <- persist(nextCommitContext)
    } yield sel 
  }

  private def persist(ctx: CommitContext): Future[Boolean] = {
    val status = getGameStatus(ctx.game.status)
    val ratings = {
      val assoc = ctx.players.map { p => (PlayerId(p.id), p.rating) }.toMap.withDefaultValue(0)
      val players = ctx.game.players.map { p => Rating.Player(p.id, p.status, assoc(p.id)) }
      Rating.compute(players)
    }
    val dbio = for {
      gc <- dal.Game.query.filter(_.id === ctx.gameId).map(_.status).update(status)
      pc <- DBIO.sequence {
        ctx.game.players.map { p =>
          val playerStatus = getPlayerStatus(p.status)
          val ratingDelta = ratings.get(p.id).map(_.delta)
          dal.Player.query
            .filter(_.id === p.id.id)
            .map { p => (p.status, p.ratingDelta) }
            .update((playerStatus, ratingDelta))
        }
      }
    } yield pc.sum + gc
    // Just ensure the returned number of rows is non-zero for now.
    queryCli.read(dbio).map(_ > 0)
  }

  private def persistRedis(ctx: CommitContext): Future[Selection] = {
    val key = ctx.gameKey
    val gameJson = GameJson.stringify(ctx.game)
    redisCli.set(key, gameJson).flatMap {
      case false => throw new RuntimeException(s"redis set failed: '$key'")
      case true => publish(ctx).map(_ => Selection(ctx.game))
    }
  }
  
  private def publish(ctx: CommitContext): Future[Int] = {
    val evt = event(ctx)
    val channels = ctx.players.map { player => StreamingChannel.gameChange(player.userId) }
    val fanout = channels.map(ch => redisCli.publish(ch, Json.stringify(evt)))
    Future.sequence(fanout.toSeq).map(_.sum.toInt)
  }
}

object PhaseCommit {
  type Input = Context[GraphqlContext, Unit]

  val NoContinuation: CommitContext => Change[Game] =
    ctx => Accept(ctx.game)

  case class CommitContext(
      // User & player who is committing the phase.
      userId: UUID,
      playerId: PlayerId,
      // Persisted players.
      players: Seq[dal.Player],
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

  private def getGameStatus(status: Game.Status): dal.GameStatus = {
    status match {
      case Game.Status.Started => dal.GameStatus.Started
      case Game.Status.Complete => dal.GameStatus.Complete
      case Game.Status.Invalid => dal.GameStatus.Invalid
    }
  }

  private def getPlayerStatus(status: Player.Status): dal.PlayerStatus = {
    status match {
      case Player.Status.Playing => dal.PlayerStatus.Playing
      case Player.Status.Won => dal.PlayerStatus.Won
      case Player.Status.Lost => dal.PlayerStatus.Lost
      case Player.Status.Drawn => dal.PlayerStatus.Drawn
      case Player.Status.Invalid => dal.PlayerStatus.Invalid
    }
  }
}
