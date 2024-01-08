package monarchy.auth

import monarchy.dal
import java.util.UUID

sealed trait Auth

/** This "user" has no access. */
case object NullAuth extends Auth

case class Authenticated(user: dal.User) extends Auth {
  def userId: UUID =
    user.id

  def admin: Boolean =
    user.membership == dal.Membership.Admin
}
