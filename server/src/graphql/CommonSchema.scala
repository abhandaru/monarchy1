package monarchy.graphql

import monarchy.game
import sangria.schema._

object CommonSchema {
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

  lazy val PhaseType =
    EnumUtil.mkEnum[game.Phase]("Phase", game.Phase.values)

  lazy val SelectionType = ObjectType(
    "Selection",
    fields[GraphqlContext, Selection](
      Field("selection", OptionType(VecType), resolve = _.value.game.currentSelection),
      Field("piece", OptionType(PieceType), resolve = _.value.game.currentPiece),
      Field("movements", ListType(VecType), resolve = _.value.movements.toSeq),
      Field("directions", ListType(VecType), resolve = _.value.directions.toSeq),
      Field("attacks", ListType(ListType(VecType)), resolve = _.value.attacks.map(_.toSeq).toSeq),
      Field("phases", ListType(PhaseType), resolve = _.value.game.currentPhases),
    )
  )
}