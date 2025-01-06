package monarchy.graphql

import sangria.schema._

object MutationSchema {
  import CommonSchema._

  lazy val Def = ObjectType(
    "Mutation",
    fields[GraphqlContext, Unit](
      Field("select", SelectionType, arguments = List(GqlArgs.Select), resolve = SelectResolver),
      Field("deselect", SelectionType, arguments = List(GqlArgs.Deselect), resolve = DeselectResolver),
      Field("move", SelectionType, arguments = List(GqlArgs.Move), resolve = MoveResolver),
      Field("attack", SelectionType, arguments = List(GqlArgs.Attack), resolve = AttackResolver),
      Field("direct", SelectionType, arguments = List(GqlArgs.Direction), resolve = DirectionResolver),
      Field("endTurn", SelectionType, arguments = List(GqlArgs.EndTurn), resolve = EndTurnResolver),
      Field("challengeSeek", QuerySchema.ChallengeType, resolve = ChallengeSeekResolver),
    )
  )
}
