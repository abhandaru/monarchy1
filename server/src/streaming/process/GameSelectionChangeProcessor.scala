package monarchy.streaming.process

import monarchy.game._
import monarchy.marshalling.game.GameJson
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.concurrent.{Future, ExecutionContext}

abstract class GameSelectionChangeProcessor[T <: StreamAction](implicit
  ec: ExecutionContext,
  redisCli: RedisClient
) extends ClientActionProcessor[T] {
  // [[StreamAction]] extractor methods
  def extractUserId(axn: T): Long
  def extractGameId(axn: T): Long

  // Define the transformation
  def gameChange(axn: T, game: Game): Change[Game]

  override def apply(axn: T): Future[Unit] = {
    val gameId = extractGameId(axn)
    val gameKey = StreamingKey.Game(gameId)
    redisCli.get[Game](gameKey).flatMap {
      case None => Async.Unit
      case Some(game) =>
        gameChange(axn, game) match {
          case r: Reject =>
            println(s"[game-selection-change] rejected=$r")
            Async.Unit
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => Async.Unit
              case true =>
                val event = GameChangeSelection(gameId)
                val channel = StreamingChannel.gameSelectTile(extractUserId(axn))
                println(s"[game-selection-change] selection updated on key=$gameKey")
                redisCli.publish(channel, Json.stringify(event))
                  .map(_ => ())
            }
        }
    }
  }
}

class GameSelectTileProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends GameSelectionChangeProcessor[GameSelectTile] {
  override def extractUserId(axn: GameSelectTile) = axn.auth.userId
  override def extractGameId(axn: GameSelectTile) = axn.body.gameId.toLong
  override def gameChange(axn: GameSelectTile, game: Game) = {
    game.tileSelect(PlayerId(extractUserId(axn)), axn.body.point)
  }
}

class GameDeselectTileProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends GameSelectionChangeProcessor[GameDeselectTile] {
  override def extractUserId(axn: GameDeselectTile) = axn.auth.userId
  override def extractGameId(axn: GameDeselectTile) = axn.body.gameId.toLong
  override def gameChange(axn: GameDeselectTile, game: Game) = {
    game.tileDeselect(PlayerId(extractUserId(axn)))
  }
}
