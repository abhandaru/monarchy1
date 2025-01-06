package monarchy.controllers

import akka.http.scaladsl.model._
import monarchy.auth.oauth2.ExchangeClient
import monarchy.util.Json
import scala.concurrent.{ExecutionContext, Future}

class DiscordAuthorizeController(implicit
    ec: ExecutionContext,
    exchangeCli: ExchangeClient
) extends GetController {
  import DiscordAuthorizeController._

  def action(ctx: AuthContext): Future[HttpResponse] = {
    val query = Uri.Query(ctx.request.request.uri.rawQueryString)
    val callerContext = mkCallerContext(query)
    val contextJson = Json.stringify(callerContext)
    exchangeCli.fetchAuthorizeUrl(contextJson).map { url =>
      HttpResponse(StatusCodes.TemporaryRedirect, headers = List(headers.Location(url)))
    }
  }
}

object DiscordAuthorizeController {
  private[controllers] def isLocal: Boolean =
    sys.env.get("ENV").nonEmpty

  /**
   * A caller context defines under what conditions the authorization flow is
   * initiated. This can have important UX implications. We can later add 
   * support for some custom base 64 data here as well.
   *
   * @param referrerUrl the URL of the calling client, if any
   * @param embedded whether the caller is embedded on the OS (aka native app)
   */
  case class CallerContext(referrerUrl: String, embedded: Boolean)

  private def mkCallerContext(query: Uri.Query): CallerContext = {
    val referrerUrl = query.getOrElse("referrerUrl", mkBaseUrl)
    val embedded = query.get("embedded").nonEmpty
    CallerContext(referrerUrl = referrerUrl, embedded = embedded)
  }

  private[controllers] def mkBaseUrl: String =
    if (isLocal) "http://localhost:8081" else "https://monarchy1.com"
}
