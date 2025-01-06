package monarchy.users

import monarchy.dal
import scala.concurrent.duration._

object Flow {
  case class Credentials(
      user: dal.User,
      bearer: String,
      ttl: FiniteDuration = 30.days
  )

  sealed trait Result

  object Result {
    case class Signup(creds: Credentials) extends Result
    case class LoggedIn(creds: Credentials) extends Result
    case class Error(reason: String) extends Result
  }
}