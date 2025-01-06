package monarchy.users

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import monarchy.auth.AuthTooling
import monarchy.dal
import monarchy.util.http.{HttpClient, UrlBuilder}

class DiscordFlow(implicit
    ec: ExecutionContext,
    queryCli: dal.QueryClient,
    httpCli: HttpClient
) {
  import DiscordFlow._
  import dal.PostgresProfile.Implicits._

  private lazy val signup = new Signup

  def apply(ctx: Context): Future[Flow.Result] = {
    for {
      profile <- fetchProfile(ctx.accessToken)
      userOpt <- fetchExisting(profile.email)
      result <- userOpt match {
        case Some(user) => handleLogin(user)
        case None => signup(Signup.Context(profile.username, profile.email))
      }
    } yield result
  }

  private def handleLogin(user: dal.User): Future[Flow.Result] = {
    val bearer = AuthTooling.generateSignature(user.id, user.secret)
    val result = Flow.Result.LoggedIn(Flow.Credentials(user, bearer))
    Future.successful(result)
  }

  private def fetchProfile(accessToken: String): Future[Profile] = {
    val req = HttpClient.Request(Discord.User.Url, headers = mkHeaders(accessToken))
    httpCli(req).map(_.as[Discord.User.Data]).map {
      case Success(data) => Profile(data.id, data.username, data.email)
      case Failure(e) => throw e
    }
  }

  private def fetchExisting(email: String): Future[Option[dal.User]] =
    queryCli.first(dal.User.query.filter(_.email === email))
}

object DiscordFlow {
  case class Context(accessToken: String)

  private case class Profile(
    id: String,
    username: String,
    email: String,
  )

  private def mkHeaders(accessToken: String): Map[String, String] =
    Map("Authorization" -> s"Bearer $accessToken")
}
