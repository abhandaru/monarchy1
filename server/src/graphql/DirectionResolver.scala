package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import scala.concurrent.Future

object DirectionResolver extends Resolver[Unit, Selection] with StrictLogging {
  import GameStringDeserializer._

  override def apply(in: In): Out = {
    // Get all the implicits
    import in.ctx._
    val args = in.arg(Args.Direction)
    val userId = expectUserId(in)
    val gameId = UUID.fromString(args.gameId)
    val gameKey = StreamingKey.Game(gameId)
    val dir = extractVec(args.direction)
    redisCli.get[Game](gameKey).flatMap {
      case None => Future.failed(NotFound(s"game '$gameId' not found"))
      case Some(game) =>
        game.directionSelect(PlayerId(userId), dir) match {
          case r: Reject => Future.failed(Rejection(r))
          case Accept(nextGame) =>
            redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
              case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
              case true =>
                val event = GameDirection(gameId)
                val channel = StreamingChannel.gameDirection(userId)
                redisCli.publish(channel, Json.stringify(event))
                  .map(_ => Selection(nextGame))
            }
        }
    }
  }

  private def extractVec(argVec: VecQuery): Vec =
    Vec(argVec.i, argVec.j)
}
