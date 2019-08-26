package monarchy.graphql

import monarchy.auth.AuthTooling
import monarchy.dal
import sangria.schema._
import scala.concurrent.ExecutionContext

case class AuthResult(
  user: Option[dal.User],
  bearerToken: Option[String]
)

object MutationSchema {
  lazy val Def = ObjectType(
    "Mutation",
    fields[GraphqlContext, Unit](
      Field("auth", BooleanType,
        arguments = List(Args.Auth),
        resolve = { node =>
          val query = node.arg(Args.Auth)
          println(s"web-server >> initiating auth: $query")
          true
        }
      ),
      Field("login", authType,
        arguments = List(Args.Login),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          import node.ctx.executionContext
          val args = node.arg(Args.Login)
          val query = dal.User.query.filter(_.phoneNumber === args.phoneNumber)
          node.ctx.queryCli.first(query).map { user =>
            val bearerToken = user.map { u => AuthTooling.generateSignature(u.id, u.secret) }
            AuthResult(user, bearerToken)
          }
        }
      )
    )
  )

  // TODO (adu): Implement following field to support account creation.
  // def loginNew = ???

  def authType = ObjectType(
    "Auth",
    fields[GraphqlContext, AuthResult](
      Field("user", OptionType(QuerySchema.UserType),
        resolve = _.value.user
      ),
      Field("userId", OptionType(StringType),
        resolve = _.value.user.map(_.id.toString)
      ),
      Field("bearerToken", OptionType(StringType),
        resolve = _.value.bearerToken
      ),
      Field("loggedIn", BooleanType,
        resolve = _.value.bearerToken.nonEmpty
      )
    )
  )
}
