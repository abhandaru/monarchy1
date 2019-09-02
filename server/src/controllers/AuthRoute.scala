package monarchy.controllers

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpCookiePair
import akka.http.scaladsl.server._
import io.jsonwebtoken.{Jwts, MalformedJwtException, SignatureException}
import monarchy.auth._
import monarchy.dal.{QueryClient, PostgresProfile, User}
import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Try, Success, Failure}

case class AuthRoute[T](filter: AuthFilter, controller: AuthController)(implicit
  ec: ExecutionContext,
  queryCli: QueryClient
) extends Route {
  import AuthRoute._

  override def apply(ctx: RequestContext): Future[RouteResult] = {
    fetchAuth(ctx).flatMap { auth =>
      filter(auth) match {
        case false => ctx.complete(Future.successful(Reject))
        case true => controller(AuthContext(auth, ctx))
      }
    }
  }

  def fetchAuth(ctx: RequestContext): Future[Auth] = {
    val authLookup = headers(ctx.request.headers).orElse(cookies(ctx.request.cookies))
    authLookup match {
      case Some((userId, bearerToken)) => fetch(userId).map {
        // Missing authentication information
        case None => NullAuth
        case Some(user) =>
          val parser = Jwts.parser.setSigningKey(user.secret)
          val subjectMaybe = Try(parser.parseClaimsJws(bearerToken).getBody.getSubject)
          subjectMaybe match {
            // Passed all challenges.
            case Success(subject) if subject == userId.toString => Authenticated(user)
            case Success(_) => NullAuth
            case Failure(_: MalformedJwtException) => NullAuth
            case Failure(_: SignatureException) => NullAuth
            case Failure(e) => throw e
          }
      }
      // Missing authentication information
      case None => Future.successful(NullAuth)
    }
  }

  def fetch(userId: Long): Future[Option[User]] = {
    import PostgresProfile.Implicits._
    queryCli.first(User.query.filter(_.id === userId))
  }
}

object AuthRoute {
  val UserBearerKey = "X-Monarchy-Bearer-Token"
  val UserIdKey = "X-Monarchy-User-Id"
  val Reject = HttpResponse(StatusCodes.Unauthorized)

  def headers(hs: Seq[HttpHeader]): Option[(Long, String)] =
    extract(hs.map { h => h.name -> h.value }.toMap)

  def cookies(cs: Seq[HttpCookiePair]): Option[(Long, String)] =
    extract(cs.map { c => c.name -> c.value }.toMap)

  def extract(props: Map[String, String]): Option[(Long, String)] = {
    for {
      rawUserId <- props.get(UserIdKey)
      userId <- Try(rawUserId.toLong).toOption
      userToken <- props.get(UserBearerKey)
    } yield (userId, userToken)
  }
}
