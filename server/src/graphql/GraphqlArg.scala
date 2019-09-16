package monarchy.graphql

import monarchy.util.Json
import sangria.macros.derive.{deriveInputObjectType => deriveInput}
import sangria.marshalling.{FromInput, ResultMarshaller}
import sangria.schema._
import scala.reflect.runtime.universe.TypeTag

abstract class GraphqlArg[T: TypeTag] {
  implicit val fromInput: FromInput[T] = new FromInput[T] {
    val marshaller = ResultMarshaller.defaultResultMarshaller
    def fromResult(node: marshaller.Node): T = {
      Json.parse[T](Json.stringify(node))
    }
  }
}

case class AuthQuery(phoneNumber: String)
object AuthQuery extends GraphqlArg[AuthQuery]

case class LoginQuery(phoneNumber: String, otp: String)
object LoginQuery extends GraphqlArg[LoginQuery]

case class GamesQuery(userId: String)
object GamesQuery extends GraphqlArg[GamesQuery]

object Args {
  // Argument types
  val Id = Argument("id", StringType, description = "ID of this entity.")
  val Auth = Argument("q", deriveInput[AuthQuery](), description = "Query to initiate auth request.")
  val Login = Argument("q", deriveInput[LoginQuery](), description = "Query to verify login credentials.")
  val Games = Argument("q", deriveInput[GamesQuery](), description = "Query for games matching the criteria.")
}


