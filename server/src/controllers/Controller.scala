package monarchy.controllers

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import scala.concurrent.{Future, ExecutionContext}

abstract class Controller(implicit ec: ExecutionContext) extends Route {
  // Client only needs to worry about forming response.
  def action(ctx: RequestContext): Future[HttpResponse]

  override def apply(ctx: RequestContext): Future[RouteResult] = {
    action(ctx).flatMap { r => ctx.complete(r) }
  }
}

abstract class PostController[T](implicit
  ec: ExecutionContext,
  um: FromEntityUnmarshaller[T]
) extends Route {
  // Client only needs to worry about forming response.
  def action(t: T): Future[HttpResponse]

  override def apply(ctx: RequestContext): Future[RouteResult] = {
    entity(as[T]) { t =>
      c => action(t).flatMap { r => ctx.complete(r) }
    }(ctx)
  }
}
