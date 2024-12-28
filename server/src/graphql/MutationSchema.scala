package monarchy.graphql

import monarchy.auth.AuthTooling
import monarchy.dal
import monarchy.util.Format
import sangria.schema._
import scala.concurrent.ExecutionContext

case class AuthResult(
  user: Option[dal.User],
  bearerToken: Option[String]
)

object MutationSchema {
  import CommonSchema._

  lazy val Def = ObjectType(
    "Mutation",
    fields[GraphqlContext, Unit](
      Field("signup", AuthType, arguments = List(GqlArgs.Signup), resolve = SignupResolver),
      Field("loginStart", BooleanType,
        arguments = List(GqlArgs.LoginStart),
        resolve = { node =>
          val query = node.arg(GqlArgs.LoginStart)
          println(s"web-server >> initiating auth: $query")
          true
        }
      ),
      Field("login", AuthType,
        arguments = List(GqlArgs.Login),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          import node.ctx.executionContext
          val args = node.arg(GqlArgs.Login)
          val phoneNumber = Format.normalizePhoneNumber(args.phoneNumber)
          val query = dal.User.query.filter(_.phoneNumber === phoneNumber)
          node.ctx.queryCli.first(query).map { user =>
            val bearerToken = user.map { u => AuthTooling.generateSignature(u.id, u.secret) }
            AuthResult(user, bearerToken)
          }
        }
      ),
      Field("select", SelectionType, arguments = List(GqlArgs.Select), resolve = SelectResolver),
      Field("deselect", SelectionType, arguments = List(GqlArgs.Deselect), resolve = DeselectResolver),
      Field("move", SelectionType, arguments = List(GqlArgs.Move), resolve = MoveResolver),
      Field("attack", SelectionType, arguments = List(GqlArgs.Attack), resolve = AttackResolver),
      Field("direct", SelectionType, arguments = List(GqlArgs.Direction), resolve = DirectionResolver),
      Field("endTurn", SelectionType, arguments = List(GqlArgs.EndTurn), resolve = EndTurnResolver),
      Field("challengeSeek", QuerySchema.ChallengeType, resolve = ChallengeSeekResolver),
    )
  )

  private val AuthType = ObjectType(
    "Auth",
    fields[GraphqlContext, AuthResult](
      Field("user", OptionType(QuerySchema.UserType), resolve = _.value.user),
      Field("userId", OptionType(StringType), resolve = _.value.user.map(_.id.toString)),
      Field("bearerToken", OptionType(StringType), resolve = _.value.bearerToken),
      Field("loggedIn", BooleanType, resolve = _.value.bearerToken.nonEmpty),
    )
  )
}
