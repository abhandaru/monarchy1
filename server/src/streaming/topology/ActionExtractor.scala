package monarchy.streaming.topology

import monarchy.auth.{Auth, Authenticated}
import monarchy.streaming.core._
import monarchy.util.Json
import scala.collection.immutable.Iterable

case class RawAction(name: String, body: Option[String])

object ActionExtractor {
  def apply(auth: Auth, rep: String): Iterable[StreamAction] = {
    val RawAction(name, body) = Json.parse[RawAction](rep)
    (name, auth, body) match {
      case ("Ping", _, None) =>
        Iterable(Ping)
      case ("ChallengeAccept", a: Authenticated, Some(s)) =>
        Iterable(ChallengeAccept(a, Json.parse[ChallengeAccept.Body](s)))
      case ("ChallengeSeek", a: Authenticated, None) =>
        Iterable(ChallengeSeek(a))
      case ("ChallengeSeekCancel", a: Authenticated, None) =>
        Iterable(ChallengeSeekCancel(a))
      case ("GameSelectTile", a: Authenticated, Some(s)) =>
        Iterable(GameSelectTile(a, Json.parse[GameSelectTile.Body](s)))
      case ("GameDeselectTile", a: Authenticated, Some(s)) =>
        Iterable(GameDeselectTile(a, Json.parse[GameDeselectTile.Body](s)))
      case _ =>
        Iterable.empty
    }
  }
}
