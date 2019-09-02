package monarchy

import scala.concurrent.Future
import akka.http.scaladsl.server.{RouteResult, RequestContext}
import monarchy.auth.Auth

package object controllers {

  case class AuthContext(auth: Auth, request: RequestContext)

  type AuthController = AuthContext => Future[RouteResult]

}
