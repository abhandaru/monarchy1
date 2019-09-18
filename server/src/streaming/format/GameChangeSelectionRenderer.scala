package monarchy.streaming.format

import monarchy.dal
import monarchy.game.{Game, Vec}
import monarchy.marshalling.game.GameStringDeserializer
import monarchy.streaming.core.{GameChangeSelection, StreamingKey}
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object GameChangeSelectionRenderer {
  case class Data(
    selection: Option[Vec],
    movements: Set[Vec],
    directions: Set[Vec],
    attacks: Set[Set[Vec]]
  )
  val EmptyData = Data(None, Set.empty, Set.empty, Set.empty)
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
          attacks = game.attacks
        )
    }
  }
}
