package monarchy.streaming.format

import java.util.UUID
import monarchy.dal
import monarchy.game.{Game, Vec}
import monarchy.marshalling.game.GameStringDeserializer
import monarchy.streaming.core.{GameChangeSelection, StreamingKey}
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object GameChangeSelectionRenderer {
  // TODO (adu): Move to marshalling package and share with GraphQL
  case class Piece(playerId: String)
  case class Data(
    selection: Option[Vec] = None,
    piece: Option[Piece] = None,
    movements: Set[Vec] = Set.empty,
    directions: Set[Vec] = Set.empty,
    attacks: Set[Set[Vec]] = Set.empty
  )

  val EmptyData = Data()
}

class GameChangeSelectionRenderer(implicit val ec: ExecutionContext, redisCli: RedisClient)
  extends BasicActionRenderer[GameChangeSelection] {
  import GameChangeSelectionRenderer._
  import GameStringDeserializer._

  override def render(axn: GameChangeSelection): Future[Data] = {
    val gameReq = redisCli.get[Game](StreamingKey.Game(axn.gameId))
    gameReq.map {
      case None => EmptyData
      case Some(game) =>
        Data(
          selection = game.currentSelection,
          movements = game.movements,
          directions = game.directions,
          attacks = game.attacks,
          piece = game.currentPiece.map { piece =>
            Piece(playerId = piece.playerId.id.toString)
          }
        )
    }
  }
}
