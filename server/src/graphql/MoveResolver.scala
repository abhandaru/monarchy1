package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import scala.concurrent.Future

object MoveResolver extends Resolver[Unit, Selection] with StrictLogging {
  import GameStringDeserializer._

  override def apply(in: In): Out = {
    // Get all the implicits
    import in.ctx._
    val args = in.arg(Args.Move)
    val userId = expectUserId(in)
    val gameId = UUID.fromString(args.gameId)
    val gameKey = StreamingKey.Game(gameId)
    redisCli.get[Game](gameKey).flatMap {
      case None => Future.failed(NotFound(s"game '$gameId' not found"))
      case Some(game) =>
        game.moveSelect(PlayerId(userId), extractPoint(args)) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
              case true =>
                val event = GameMove(gameId)
                val channel = StreamingChannel.gameMove(userId)
                logger.info(s"move made on key=$gameKey")
                redisCli.publish(channel, Json.stringify(event))
                  .map(_ => Selection(nextGame))
            }
        }
    }
  }

  private def extractPoint(args: MoveQuery) =
    Vec(args.point.i, args.point.j)
}
