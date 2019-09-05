package monarchy.streaming

import scala.language.implicitConversions

sealed class StreamingKey(suffix: String) {
  override def toString: String = s"monarchy/streaming/$suffix"
}

sealed class StreamingScanKey(suffix: String) extends StreamingKey(s"$suffix/*") {
  def prefix: String = toString.dropRight(1)
}

object StreamingKey {
  implicit def render(k: StreamingKey): String = k.toString
  case object ChallengeScan extends StreamingScanKey("challenge")
  case class Challenge(userId: Long) extends StreamingKey(s"challenge/$userId")
}
