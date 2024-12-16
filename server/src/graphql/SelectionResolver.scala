package monarchy.graphql

import com.typesafe.scalalogging.StrictLogging
import java.util.UUID
import monarchy.game._
import monarchy.marshalling.game.{GameJson, GameStringDeserializer}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import scala.concurrent.Future

trait SelectionResolver extends Resolver[Unit, Selection] with StrictLogging {
  import GameStringDeserializer._

  // Define the selection mutation
  def extractGameId(in: In): UUID
  def extractPoint(in: In): Vec
  def change(game: Game, userId: UUID, point: Vec): Change[Game]

  override def apply(in: In): Out = {
    // Get all the implicits
    import in.ctx._
    val args = in.arg(Args.Select)
    val userId = expectUserId(in)
    val gameId = extractGameId(in)
    val gameKey = StreamingKey.Game(gameId)
    redisCli.get[Game](gameKey).flatMap {
      case None => Future.failed(NotFound(s"game '$gameId' not found"))
      case Some(game) =>
        val point = extractPoint(in)
        val noop = game.currentSelection.contains(point)
        if (noop) Async(Selection(game))
        else {
          change(game, userId, point) match {
            case r: Reject => Future.failed(Rejection(r))
            case Accept(nextGame) =>
              redisCli.set(gameKey, GameJson.stringify(nextGame)).flatMap {
                case false => throw new RuntimeException(s"redis set failed: '$gameKey'")
                case true =>
                  val event = GameChangeSelection(gameId)
                  val channel = StreamingChannel.gameSelectTile(userId)
                  logger.info(s"selection updated on key=$gameKey")
                  redisCli.publish(channel, Json.stringify(event))
                    .map(_ => Selection(nextGame))
              }
          }
        }
    }
  }
}

object SelectResolver extends SelectionResolver {
  override def extractGameId(in: In) =
    UUID.fromString(in.arg(Args.Select).gameId)

  override def extractPoint(in: In) = {
    val VecQuery(i, j) = in.arg(Args.Select).point
    Vec(i, j)
  }

  override def change(game: Game, userId: UUID, point: Vec): Change[Game] =
    game.tileSelect(PlayerId(userId), point)
}

object DeselectResolver extends SelectionResolver {
  override def extractGameId(in: In) =
    UUID.fromString(in.arg(Args.Deselect).gameId)

  override def extractPoint(in: In) = Vec.Zero

  override def change(game: Game, userId: UUID, point: Vec): Change[Game] =
    game.tileDeselect(PlayerId(userId))
}
