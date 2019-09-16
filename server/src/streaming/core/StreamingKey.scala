package monarchy.streaming.core

import scala.language.implicitConversions

sealed abstract class StreamingKey(suffix: String) {
  override def toString: String = s"monarchy/streaming/$suffix"
}

sealed class StreamingScanKey(suffix: String) extends StreamingKey(s"$suffix/*") {
  def prefix: String = toString.dropRight(1)
}

object StreamingKey {
  implicit def render(k: StreamingKey): String = k.toString
  case object ChallengeScan extends StreamingScanKey("challenge")
  case class Challenge(userId: Long) extends StreamingKey(s"challenge/$userId")
  case class Game(gameId: Long) extends StreamingKey(s"game/$gameId")
}
