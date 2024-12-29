package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._

class StatusController(implicit ec: ExecutionContext) extends GetController {
  override def action(ctx: AuthContext) = {
    Future.successful(HttpResponse(StatusCodes.OK, entity = "OK"))
  }
}

