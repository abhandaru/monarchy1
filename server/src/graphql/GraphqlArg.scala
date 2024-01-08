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

case class LoginStartQuery(phoneNumber: String)
object LoginStartQuery extends GraphqlArg[LoginStartQuery]

case class LoginQuery(phoneNumber: String, otp: String)
object LoginQuery extends GraphqlArg[LoginQuery]

case class GameQuery(gameId: String)
object GameQuery extends GraphqlArg[GameQuery]

case class GamesQuery(userId: String)
object GamesQuery extends GraphqlArg[GamesQuery]

case class VecQuery(i: Int, j: Int)
object VecQuery extends GraphqlArg[VecQuery]

case class SelectQuery(gameId: String, point: VecQuery)
object SelectQuery extends GraphqlArg[SelectQuery]

object Args {
  implicit val VecQueryMacro = deriveInput[VecQuery]()

  // Argument types
  val Id = Argument("id", StringType, description = "ID of this entity.")
  val LoginStart = Argument("q", deriveInput[LoginStartQuery](), description = "Query to initiate auth request.")
  val Login = Argument("q", deriveInput[LoginQuery](), description = "Query to verify login credentials.")
  val Games = Argument("q", deriveInput[GamesQuery](), description = "Query for games matching the criteria.")
  val Select = Argument("q", deriveInput[SelectQuery](), description = "Select a tile")
  val Deselect = Argument("q", deriveInput[GameQuery](), description = "Deselect all tiles")
}


