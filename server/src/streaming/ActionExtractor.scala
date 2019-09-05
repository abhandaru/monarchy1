package monarchy.streaming

import monarchy.auth.{Auth, Authenticated}
import monarchy.util.Json
import scala.collection.immutable.Iterable

case class RawAction(name: String, body: Option[String])

object ActionExtractor {
  def apply(auth: Auth, rep: String): Iterable[StreamAction] = {
    val RawAction(name, body) = Json.parse[RawAction](rep)
    (auth, name) match {
      case (auth: Authenticated, "ChallengeSeek") => Iterable(ChallengeSeek(auth))
      case _ => Iterable(Ping)
    }
  }
}
