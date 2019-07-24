package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server._
import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

abstract class Controller(implicit ec: ExecutionContext) extends Route {

  // Client only needs to worry about forming response.
  def action(ctx: RequestContext): Future[HttpResponse]

  // Handle marshalling etc.
  override def apply(ctx: RequestContext): Future[RouteResult] = {
    action(ctx).flatMap { r => ctx.complete(r) }
  }
}

// abstract class Controller2[T, U](
//   implicit
//   ec: ExecutionContext,
//   mt: Marshaller[RequestContext, T],
//   mu: Marshaller[U, RouteResult],
//   um: akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller[T]
// ) extends Route {
//   // Client only needs to worry about forming response.
//   def action(u: T): Future[U]

//   override def apply(ctx: RequestContext): Future[RouteResult] = {
//     val applicable = entity(as[T])
//     applicable.flatMap(action).flatMap { r => ctx.complete(r) }
//   }
// }
