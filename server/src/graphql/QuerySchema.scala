package monarchy.graphql

import java.util.UUID
import monarchy.dal
import monarchy.game
import monarchy.marshalling.game.GameStringDeserializer
import monarchy.util.Json
import sangria.schema._
import scala.concurrent.ExecutionContext

object QuerySchema {
  lazy val Def = ObjectType(
    "Query",
    fields[GraphqlContext, Unit](
      Field("user", OptionType(UserType),
        arguments = List(Args.Id),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          val id = UUID.fromString(node.arg(Args.Id))
          val query = dal.User.query.filter(_.id === id)
          node.ctx.queryCli.first(query)
        }
      ),
      Field("game", OptionType(GameType),
        arguments = List(Args.Id),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          val id = UUID.fromString(node.arg(Args.Id))
          val query = dal.Game.query.filter(_.id === id)
          node.ctx.queryCli.first(query)
        }
      ),
      Field("games", ListType(GameType),
        arguments = List(Args.Games),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          val userId = UUID.fromString(node.arg(Args.Games).userId)
          val query = dal.Player.query
            .filter(_.userId === userId)
            .join(dal.Game.query).on(_.gameId === _.id)
            .map(_._2)
            .sortBy(_.id.desc)
          node.ctx.queryCli.all(query)
        }
      ),
      Field("lobby", LobbyType, resolve = LobbyResolver),
    )
  )

  lazy val UserType = ObjectType(
    "User",
    fields[GraphqlContext, dal.User](
      Field("id", StringType, resolve = _.value.id.toString),
      Field("username", StringType, resolve = _.value.username),
      Field("rating", IntType, resolve = _.value.rating),
    )
  )

  lazy val PlayerType = ObjectType(
    "Player",
    fields[GraphqlContext, dal.Player](
      Field("status", StringType, resolve = _.value.status.toString),
      Field("user", OptionType(UserType), resolve = { node =>
        import dal.PostgresProfile.Implicits._
        val userId = node.value.userId
        val query = dal.User.query.filter(_.id === userId)
        node.ctx.queryCli.first(query)
      })
    )
  )

  lazy val GameType = ObjectType(
    "Game",
    fields[GraphqlContext, dal.Game](
      Field("id", StringType, resolve = _.value.id.toString),
      Field("status", StringType, resolve = _.value.status.toString),
      Field("players", ListType(PlayerType), resolve = { node =>
        import dal.PostgresProfile.Implicits._
        val gameId = node.value.id
        val query = dal.Player.query.filter(_.gameId === gameId)
        node.ctx.queryCli.all(query)
      }),
      Field("state", OptionType(GameStateType), resolve = { node =>
        import node.ctx.executionContext
        import GameStringDeserializer._
        val gameId = node.value.id
        node.ctx.redisCli.get[game.Game](s"monarchy/streaming/game/$gameId")
      })
    )
  )

  lazy val PhaseType = EnumUtil.mkEnum[game.Phase]("Phase", game.Phase.values)
  lazy val GameStateType = ObjectType(
    "GameState",
    fields[GraphqlContext, game.Game](
      Field("currentPlayerId", StringType, resolve = _.value.currentPlayer.id.id.toString),
      Field("currentSelection", OptionType(VecType), resolve = _.value.currentSelection),
      Field("currentPhases", ListType(PhaseType), resolve = _.value.currentPhases),
      Field("currentPiece", OptionType(PieceType), resolve = _.value.currentPiece),
      Field("tiles", ListType(TileType), resolve = _.value.board.tiles)
    )
  )

  lazy val TileType =  ObjectType(
    "Tile",
    fields[GraphqlContext, game.Tile](
      Field("point", VecType, resolve = _.value.point),
      Field("piece", OptionType(PieceType), resolve = _.value.piece)
    )
  )

  lazy val VecType = ObjectType(
    "Vec",
    fields[GraphqlContext, game.Vec](
      Field("i", IntType, resolve = _.value.i),
      Field("j", IntType, resolve = _.value.j)
    )
  )

  lazy val PieceType = ObjectType(
    "Piece",
    fields[GraphqlContext, game.Piece](
      Field("id", StringType, resolve = _.value.id.id.toString),
      Field("order", StringType, resolve = _.value.conf.toString),
      Field("name", StringType, resolve = _.value.conf.name),
      Field("playerId", StringType, resolve = _.value.playerId.id.toString),
      Field("currentHealth", IntType, resolve = _.value.currentHealth),
      Field("currentWait", IntType, resolve = _.value.currentWait),
      Field("currentDirection", VecType, resolve = _.value.currentDirection),
      Field("currentEffects", ListType(StringType), resolve = { node =>
        node.value.currentEffects.collect {
          case game.PieceEffect(_, e: game.Paralyze) => "Paralyzed"
        }
      }),
      Field("currentFocus", BooleanType, resolve = _.value.currentFocus),
      Field("currentBlocking", FloatType, resolve = { node =>
        node.value.conf.blocking + node.value.blockingAjustment
      })
    )
  )

  lazy val ChallengeType = ObjectType(
    "Challenge",
    fields[GraphqlContext, Challenge.Data](
      Field("host", QuerySchema.UserType, resolve = _.value.host),
      Field("expireAt", OptionType(StringType), resolve = _.value.expireAt.toString),
    )
  )

  lazy val LobbyType = ObjectType(
    "Lobby",
    fields[GraphqlContext, Lobby.Data](
      Field("usersOnline", ListType(UserType), resolve = _.value.online),
      Field("challenges", ListType(ChallengeType), resolve = _.value.challenges),
    )
  )
}
