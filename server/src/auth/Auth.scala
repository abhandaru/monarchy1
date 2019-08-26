package monarchy.auth

import monarchy.dal.User

sealed trait Auth

/** This "user" has no access. */
case object NullAuth extends Auth
case class Authenticated(user: User) extends Auth
