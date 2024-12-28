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
      Field("signup", AuthType, arguments = List(Args.Signup), resolve = SignupResolver),
      Field("loginStart", BooleanType,
        arguments = List(Args.LoginStart),
        resolve = { node =>
          val query = node.arg(Args.LoginStart)
          println(s"web-server >> initiating auth: $query")
          true
        }
      ),
      Field("login", AuthType,
        arguments = List(Args.Login),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          import node.ctx.executionContext
          val args = node.arg(Args.Login)
          val phoneNumber = Format.normalizePhoneNumber(args.phoneNumber)
          val query = dal.User.query.filter(_.phoneNumber === phoneNumber)
          node.ctx.queryCli.first(query).map { user =>
            val bearerToken = user.map { u => AuthTooling.generateSignature(u.id, u.secret) }
            AuthResult(user, bearerToken)
          }
        }
      ),
      Field("select", SelectionType, arguments = List(Args.Select), resolve = SelectResolver),
      Field("deselect", SelectionType, arguments = List(Args.Deselect), resolve = DeselectResolver),
      Field("move", SelectionType, arguments = List(Args.Move), resolve = MoveResolver),
      Field("attack", SelectionType, arguments = List(Args.Attack), resolve = AttackResolver),
      Field("direct", SelectionType, arguments = List(Args.Direction), resolve = DirectionResolver),
      Field("endTurn", SelectionType, arguments = List(Args.EndTurn), resolve = EndTurnResolver),
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
