package monarchy.streaming

import scala.language.implicitConversions

sealed abstract class StreamingKey[T](suffix: String) {
  type Value = T
  override def toString: String = s"monarchy/streaming/$suffix"
}

sealed class StreamingScanKey(suffix: String) extends StreamingKey[Nothing](s"$suffix/*") {
  def prefix: String = toString.dropRight(1)
}

object StreamingKey {
  implicit def render(k: StreamingKey[_]): String = k.toString
  case object ChallengeScan extends StreamingScanKey("challenge")
  case class Challenge(userId: Long) extends StreamingKey[Boolean](s"challenge/$userId")
}
