package monarchy.graphql

import monarchy.dal
import monarchy.game
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
          val id = node.arg(Args.Id).toLong
          val query = dal.User.query.filter(_.id === id)
          node.ctx.queryCli.first(query)
        }
      ),
      Field("game", OptionType(GameType),
        arguments = List(Args.Id),
        resolve = { node =>
          game.GameBuilder(
            seed = 77,
            players = Seq(
              game.Player(game.PlayerId(2L), Seq((game.Vec(6, 4), game.Knight), (game.Vec(5, 6), game.FrostGolem))),
              game.Player(game.PlayerId(1L), Seq((game.Vec(7, 7), game.Assassin), (game.Vec(8, 6), game.Scout)))
            )
          )
        }
      )
    )
  )

  lazy val UserType = ObjectType(
    "User",
    fields[GraphqlContext, dal.User](
      Field("id", StringType,
        resolve = _.value.id.toString
      ),
      Field("username", StringType,
        resolve = _.value.username
      )
    )
  )

  lazy val GameType = ObjectType(
    "Game",
    fields[GraphqlContext, game.Game](
      Field("id", StringType,
        resolve = _ => "abc123"
      )
    )
  )
}
