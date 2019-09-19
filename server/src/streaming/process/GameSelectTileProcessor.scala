package monarchy.streaming.process

import monarchy.game._
import monarchy.marshalling.game.GameJson
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.concurrent.{Future, ExecutionContext}

class GameSelectTileProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends ClientActionProcessor[GameSelectTile] {
  override def apply(axn: GameSelectTile): Future[Unit] = {
    val userId = axn.auth.userId
    val gameId = axn.body.gameId.toLong
    val gameKey = StreamingKey.Game(gameId)
    redisCli.get[Game](gameKey).flatMap {
      case None => Async.Unit
      case Some(game) =>
        val gameChange = game.tileSelect(PlayerId(userId), axn.body.point)
        gameChange match {
          case r: Reject =>
            println(s"[game-select-tile] rejected=$r")
            Async.Unit
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => Async.Unit
              case true =>
                val event = GameChangeSelection(gameId)
                println(s"[game-select-tile] selected tile ${axn.body.point} on key=$gameKey")
                redisCli.publish(StreamingChannel.gameSelectTile(userId), Json.stringify(event))
                  .map(_ => ())
            }
        }
    }
  }
}
