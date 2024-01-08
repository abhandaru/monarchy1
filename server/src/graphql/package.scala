package monarchy

import sangria.schema.Schema
import monarchy.game.Reject

package object graphql {
  val GraphqlSchema = Schema(QuerySchema.Def, Some(MutationSchema.Def))

  case object NotAuthorized extends RuntimeException("not authorized")
  case class NotFound(m: String) extends RuntimeException(m)
  case class Rejection(r: Reject) extends RuntimeException(s"rejected: $r")
}
