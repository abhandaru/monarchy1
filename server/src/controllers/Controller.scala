package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server._

abstract class Controller(implicit ec: ExecutionContext)
  extends (RequestContext => Future[RouteResult]) {

  // Client only needs to worry about forming response.
  def action(ctx: RequestContext): Future[HttpResponse]

  // Handle marshalling etc.
  override def apply(ctx: RequestContext): Future[RouteResult] = {
    action(ctx).flatMap { r => ctx.complete(r) }
  }
}
