package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import monarchy.dal

abstract class Controller(implicit ec: ExecutionContext)
  extends (RequestContext => Future[RouteResult]) {

  // Client only needs to worry about forming response.
  def action(ctx: RequestContext): Future[HttpResponse]

  // Handle marshalling etc.
  override def apply(ctx: RequestContext): Future[RouteResult] = {
    action(ctx).flatMap { r => ctx.complete(r) }
  }
}

class RootController(implicit ec: ExecutionContext, queryCli: dal.QueryClient) extends Controller {
  import dal.PostgresProfile.Implicits._
  override def action(ctx: RequestContext) = {
    val q = dal.UserQuery.filter(_.id === 1L)
    queryCli.first(q).map { userMaybe =>
      HttpResponse(StatusCodes.OK, entity = userMaybe.toString)
    }
  }
}
