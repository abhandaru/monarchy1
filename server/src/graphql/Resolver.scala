package monarchy.graphql

import java.util.UUID
import monarchy.auth._
import sangria.schema.{Action, Context}

trait Resolver[Q, R] extends (Context[GraphqlContext, Q] => Action[GraphqlContext, R]) {
  // These are useful for abbreviating implementations.
  type In = Context[GraphqlContext, Q]
  type Out = Action[GraphqlContext, R]

  // Helper for extracting user ID or throwing
  protected def expectUserId(in: In): UUID =
    Resolver.expectUserId[Q](in)
}

object Resolver {
  // Helper for extracting user ID or throwing
  def expectUserId[Q](in: Context[GraphqlContext, Q]): UUID = {
    in.ctx.auth match {
      case NullAuth => throw NotAuthorized
      case Authenticated(u) => u.id
    }
  }
}
