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

case class SignupQuery(e164: String, username: String, otp: String)
object SignupQuery extends GraphqlArg[SignupQuery]

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

case class MoveQuery(gameId: String, point: VecQuery)
object MoveQuery extends GraphqlArg[MoveQuery]

case class AttackQuery(gameId: String, attack: Seq[VecQuery])
object AttackQuery extends GraphqlArg[AttackQuery]

case class DirectionQuery(gameId: String, direction: VecQuery)
object DirectionQuery extends GraphqlArg[DirectionQuery]

case class EndTurnQuery(gameId: String)
object EndTurnQuery extends GraphqlArg[EndTurnQuery]

object GqlArgs {
  implicit val VecQueryMacro: InputObjectType[VecQuery] =
    deriveInput[VecQuery]()

  // Argument types
  val Id = Argument("id", StringType, description = "ID of this entity.")
  val Signup = Argument("q", deriveInput[SignupQuery](), description = "Signup query")
  val LoginStart = Argument("q", deriveInput[LoginStartQuery](), description = "Query to initiate auth request")
  val Login = Argument("q", deriveInput[LoginQuery](), description = "Query to verify login credentials")
  val Games = Argument("q", deriveInput[GamesQuery](), description = "Query for games matching the criteria")
  val Select = Argument("q", deriveInput[SelectQuery](), description = "Select a tile")
  val Deselect = Argument("q", deriveInput[GameQuery](), description = "Deselect all tiles")
  val Move = Argument("q", deriveInput[MoveQuery](), description = "Move to tile")
  val Attack = Argument("q", deriveInput[AttackQuery](), description = "Attack a tile")
  val Direction = Argument("q", deriveInput[DirectionQuery](), description = "Orient a piece on a tile")
  val EndTurn = Argument("q", deriveInput[EndTurnQuery](), description = "End current turn")
}


