package monarchy.controllers

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import monarchy.auth.Auth
import scala.concurrent.{Future, ExecutionContext}

abstract class AuthController
  extends (AuthContext => Future[RouteResult])

abstract class GetController(implicit ec: ExecutionContext) extends AuthController {
  // Client only needs to worry about forming response.
  def action(ctx: AuthContext): Future[HttpResponse]

  override def apply(ctx: AuthContext): Future[RouteResult] = {
    action(ctx).flatMap { r => ctx.request.complete(r) }
  }
}

abstract class PostController[T](implicit
  ec: ExecutionContext,
  um: FromEntityUnmarshaller[T]
) extends AuthController {
  // Client only needs to worry about forming response.
  def action(ctx: AuthContext, t: T): Future[HttpResponse]

  override def apply(ctx: AuthContext): Future[RouteResult] = {
    entity(as[T]) { t =>
      c => action(ctx, t).flatMap { r => ctx.request.complete(r) }
    }(ctx.request)
  }
}
