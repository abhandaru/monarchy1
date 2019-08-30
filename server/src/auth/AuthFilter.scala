package monarchy.auth

object AuthFilter {
  // TODO (adu): Replace with something role based.
  private val AdminIds: Set[Long] = Set(1L)

  /** Just let every request through. Useful for debugging */
  val All: AuthFilter = _ => true

  /** Just make sure the user is logged in; verified `userId` and `bearerToken` */
  val LoggedIn: AuthFilter = _ != NullAuth

  /** Intended for access to management and debugging tools */
  val Admin: AuthFilter = {
    case NullAuth => false
    case Authenticated(u) => AdminIds(u.id)
  }
}
