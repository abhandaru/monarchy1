package monarchy.controllers

import scala.concurrent.Future
import akka.http.scaladsl.model._

object DiscordAuthorizeController extends PostController[String] {
  def action(ctx: AuthContext, t: String): Future[HttpResponse] = {
    Future.successful(HttpResponse(StatusCodes.OK))
  }
}

object DiscordExchangeController extends PostController[String] {
  def action(ctx: AuthContext, t: String): Future[HttpResponse] = {
    Future.successful(HttpResponse(StatusCodes.OK))
  }
}
