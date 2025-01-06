package monarchy.controllers

import akka.http.scaladsl.model._
import monarchy.auth.oauth2.ExchangeClient
import monarchy.util.Json
import scala.concurrent.{ExecutionContext, Future}

class DiscordAuthorizeController(implicit
    ec: ExecutionContext,
    exchangeCli: ExchangeClient
) extends GetController {
  import DiscordExchangeController._

  def action(ctx: AuthContext): Future[HttpResponse] = {
    val stateJson = Json.stringify(State(referrerUrl = "http://localhost:8081"))
    exchangeCli.fetchAuthorizeUrl(stateJson).map { url =>
      HttpResponse(StatusCodes.TemporaryRedirect, headers = List(headers.Location(url)))
    }
  }
}
