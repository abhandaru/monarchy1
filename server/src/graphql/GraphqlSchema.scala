package monarchy.graphql

import monarchy.dal
import sangria.execution.FieldTag
import sangria.schema._
import scala.concurrent.ExecutionContext

object Args {
  val Id = Argument("id", StringType, description = "ID of this entity.")
}

object GraphqlSchema {
  val Def = Schema(querySchema)

  def querySchema = ObjectType(
    "Query",
    fields[GraphqlContext, Unit](
      Field("user", OptionType(userSchema),
        arguments = List(Args.Id),
        resolve = { node =>
          import dal.PostgresProfile.Implicits._
          val id = node.arg(Args.Id).toLong
          val query = dal.User.query.filter(_.id === id)
          node.ctx.queryCli.first(query)
        }
      )
    )
  )

  def userSchema = ObjectType(
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
}
