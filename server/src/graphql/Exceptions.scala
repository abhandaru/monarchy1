package monarchy.graphql

object Exceptions {
  case class BadArgs(msg: String) extends RuntimeException(msg)
}
