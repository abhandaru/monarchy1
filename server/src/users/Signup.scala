package monarchy.users

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import monarchy.auth.Tooling
import monarchy.dal
import monarchy.dalwrite.WriteQueryBuilder
import monarchy.util.Async

class Signup(implicit
    ec: ExecutionContext,
    queryCli: dal.QueryClient
) {
  import dal.PostgresProfile.Implicits._
  import Signup._
  import Flow._

  def apply(ctx: Context): Future[Result] = {
    val email = ctx.email.trim
    val username = ctx.username.trim
    if (!validEmail(email)) Future.successful(Result.Error("invalid email"))
    else if (!validUsername(username)) Future.successful(Result.Error("invalid username"))
    else {
      val existingUsername = queryCli.count(dal.User.query.filter(_.username === username)).map(_ > 0)
      val existingEmail = queryCli.count(dal.User.query.filter(_.email === email)).map(_ > 0)
      Async.join(existingUsername, existingEmail).flatMap {
        case (true, _) => Future.successful(Result.Error("username in use"))
        case (_, true) => Future.successful(Result.Error("email in use"))
        case _ =>
          val secret = Tooling.generateSecret
          val user = dal.User(email = email, username = username, secret = secret)
          val dbio = for {
            userWr <- WriteQueryBuilder.put(user)
            profileWr <- WriteQueryBuilder.put(mkProfile(userWr.id))
          } yield userWr
          queryCli.write(dbio).map { u =>
            val bearer = Tooling.generateSignature(u.id, secret)
            Result.Signup(Credentials(u, bearer))
          }
      }
    }
  }
}

object Signup {
  case class Context(
      username: String,
      email: String,
  )

  private def validEmail(email: String): Boolean =
    email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

  private def validUsername(username: String): Boolean =
    username.matches("^[a-zA-Z0-9_]{3,20}$")

  private def mkProfile(userId: UUID): dal.Profile = {
    val colorIndex = math.abs(userId.hashCode) % Colors.length
    val color = Colors(colorIndex)
    dal.Profile(userId = userId, avatar = "Knight", color = color)
  }

  // Expand these as necessary. Took inspiration from:
  // https://www.color-hex.com/color-palette/1053990
  private val Colors = Array(
    "#440001",
    "#6d1313",
    "#233651",
    "#ecb84a",
    "#d9d2c5"
  )
}
