package monarchy.graphql

import java.util.UUID
import scala.concurrent.Future
import monarchy.auth.AuthTooling
import monarchy.dal
import monarchy.dalwrite.WriteQueryBuilder
import monarchy.util.Async

object SignupResolver extends Resolver[Unit, AuthResult] {
  import dal.PostgresProfile.Implicits._

  override def apply(in: In): Out = {
    import in.ctx._
    val args = in.arg(Args.Signup)
    val e164 = args.e164.trim
    val username = args.username.trim
    if (!validE164(e164)) Future.failed(Exceptions.BadArgs("invalid e164"))
    else if (!validUsername(username)) Future.failed(Exceptions.BadArgs("invalid username"))
    else {
      val existingUsername = queryCli.count(dal.User.query.filter(_.username === username)).map(_ > 0)
      val existingE164 = queryCli.count(dal.User.query.filter(_.phoneNumber === e164)).map(_ > 0)
      Async.join(existingUsername, existingE164).flatMap {
        case (true, _) => Future.failed(Exceptions.BadArgs("username in use"))
        case (_, true) => Future.failed(Exceptions.BadArgs("e164 in use"))
        case _ =>
          val secret = AuthTooling.generateSecret
          val user = dal.User(phoneNumber = e164, username = username, secret = secret)
          val dbio = for {
            userWr <- WriteQueryBuilder.put(user)
            profileWr <- WriteQueryBuilder.put(mkProfile(userWr.id))
          } yield userWr
          queryCli.write(dbio).map { u =>
            val bearer = AuthTooling.generateSignature(u.id, secret)
            AuthResult(Some(u), Some(bearer))
          }
      }
    }
  }

  private def validE164(e164: String): Boolean =
    e164.matches("^\\+[1-9]\\d{1,14}$")

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
