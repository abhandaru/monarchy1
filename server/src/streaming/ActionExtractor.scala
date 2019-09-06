package monarchy.streaming

import monarchy.auth.{Auth, Authenticated}
import monarchy.util.Json
import scala.collection.immutable.Iterable

case class RawAction(name: String, body: Option[String])

object ActionExtractor {
  def apply(auth: Auth, rep: String): Iterable[StreamAction] = {
    val RawAction(name, body) = Json.parse[RawAction](rep)
    (auth, name, body) match {
      case (a: Authenticated, "ChallengeAccept", Some(s)) => Iterable(ChallengeAccept(a, Json.parse[ChallengeAccept.Body](s)))
      case (a: Authenticated, "ChallengeSeek", None) => Iterable(ChallengeSeek(a))
      case (a: Authenticated, "ChallengeSeekCancel", None) => Iterable(ChallengeSeekCancel(a))
      case _ => Iterable(Ping)
    }
  }
}
