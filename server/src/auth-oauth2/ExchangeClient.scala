package monarchy.auth.oauth2

import akka.http.scaladsl.model.FormData
import java.net.URLEncoder
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Random}
import monarchy.util.{Base64Bijection, Json}

class ExchangeClient(config: Oauth2.Config)(
    implicit
    ec: ExecutionContext,
    httpCli: HttpClient
) {
  import ExchangeClient._

  // TODO (adu): Once we have multiple replicas, we will need to use Redis
  // here instead.
  private val store = new Store.InMemory[String]
  
  def fetchAuthorizeUrl(state: String): Future[String] = {
    import config._
    val scope = mkScope
    val token = mkStateToken
    val url = s"$baseUrl/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code&scope=$scope&state=$token"
    store.set(token, state, TokenTtl).map(_ => url)
  }

  def fetchToken(code: String, token: String): Future[Exchange] = {
    store.get(token).flatMap {
      case None => Future.successful(Exchange.BadToken(token))
      case Some(state) =>
        val data = Oauth2.Token.Request(code = Some(code), redirectUri = Some(config.redirectUri), scope = Some(mkScope))
        val req = HttpClient.Request(
          url = s"${config.baseUrl}/token",
          headers = mkTokenHeaders,
          body = Some(mkTokenData(data))
        ).asPost
        httpCli(req).map(_.as[Oauth2.Token.Response]).map {
          case Success(token) => Exchange.Ok(token, state)
          case Failure(e) => throw e
        }
    }
  }

  private def mkTokenHeaders: Map[String, String] = {
    import config._
    val token = Base64Bijection[String].apply(s"$clientId:$clientSecret")
    Map("Authorization" -> s"Basic $token")
  }

  private def mkScope: String = {
    val scope = config.scopes.mkString(" ")
    URLEncoder.encode(scope, "UTF-8")
  }
}

object ExchangeClient {
  private val TokenTtl = 10.minutes

  sealed trait Exchange

  object Exchange {
    case class Ok(token: Oauth2.Token.Response, state: String) extends Exchange
    case class BadToken(token: String) extends Exchange
  }

  private def mkStateToken: String =
    Random.alphanumeric.take(32).mkString

  private def mkTokenData(data: Oauth2.Token.Request): FormData = {
    val map = Json.parse[Map[String, String]](Json.stringify(data))
    println(s"map: $map")
    FormData(map)
  }
}
