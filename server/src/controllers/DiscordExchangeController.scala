package monarchy.controllers

import akka.http.scaladsl.model._
import java.net.URLEncoder
import monarchy.auth.oauth2.{ExchangeClient, Oauth2}
import monarchy.util.Json
import scala.concurrent.{ExecutionContext, Future}

class DiscordExchangeController(implicit
    ec: ExecutionContext,
    exchangeCli: ExchangeClient
) extends GetController {
  import DiscordExchangeController._
  import ExchangeClient._
  import Oauth2.Callback

  def action(ctx: AuthContext): Future[HttpResponse] = {
    val query = Uri.Query(ctx.request.request.uri.rawQueryString)
    val data = Callback.parse(query)
    data match {
      case Callback.ErrorOther =>
        Future.successful(HttpResponse(StatusCodes.BadRequest))
      case Callback.Error(error, desc) =>
        Future.successful(HttpResponse(StatusCodes.BadRequest))
      case Callback.Success(code, state) =>
        redirect(code, state)
    }
  }


  /**
   * After exchanging the code for the token, we check the `CallerContext` to
   * see if we should redirect to the referrer or return the token as a JSON
   * response. The latter is used for embedded callers (i.e. native apps).
   *
   * @param code the code we received from Discord
   * @param token We call `state` a token once we get to this layer, since it
   *   is just some nonce we generated.
   */
  private def redirect(code: String, token: String): Future[HttpResponse] = {
    exchangeCli.fetchToken(code, token).map {
      case Exchange.BadToken(token) =>
        HttpResponse(StatusCodes.BadRequest)
      case Exchange.Ok(token, contextJson) =>
        val callerContext = Json.parse[DiscordAuthorizeController.CallerContext](contextJson)
        if (callerContext.embedded) {
          val credentialsJson = Json.stringify(token)
          val entity = HttpEntity(ContentTypes.`application/json`, credentialsJson)
          HttpResponse(StatusCodes.OK, entity = entity)
        } else {
          val location = headers.Location(callerContext.referrerUrl)
          val cookie = mkCookieHeader(token.accessToken, token.expiresIn)
          HttpResponse(StatusCodes.TemporaryRedirect, headers = List(location, cookie))
        }
    }
  }
}

object DiscordExchangeController {
  private def mkCookieHeader(jwt: String, ttl: Int): HttpHeader = {
    headers.`Set-Cookie`(headers.HttpCookie(
      name = "Authorization",
      value = URLEncoder.encode(s"Bearer $jwt", "UTF-8"),
      maxAge = Some(ttl),
      path = Some("/"),
      secure = !DiscordAuthorizeController.isLocal,
      httpOnly = true
    ))
  }
}
