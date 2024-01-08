package monarchy.streaming.core

import akka.actor.ActorRef
import java.util.UUID
import monarchy.auth.Authenticated
import monarchy.game.Vec

sealed trait StreamAction

/**
 * Inbound actions
 *
 * These are actions flowing into the system from the client. All of them
 * should have an associated [[Authenticated]] context to inform user access
 * and identity.
 */
case object Ping extends StreamAction

case class ChallengeAccept(auth: Authenticated, body: ChallengeAccept.Body) extends StreamAction
object ChallengeAccept {
  case class Body(opponentId: String)
}

case class ChallengeSeek(auth: Authenticated) extends StreamAction
case class ChallengeSeekCancel(auth: Authenticated) extends StreamAction

case class GameSelectTile(auth: Authenticated, body: GameSelectTile.Body) extends StreamAction
object GameSelectTile {
  case class Body(gameId: String, point: Vec)
}

case class GameDeselectTile(auth: Authenticated, body: GameDeselectTile.Body) extends StreamAction
object GameDeselectTile {
  case class Body(gameId: String)
}

/**
 * Intermediate actions
 *
 * These are sent between actors in the same topology. This includes connecting
 * to other actors and proxying data-sources to merge into the output stream.
 */
case class Connect(next: ActorRef) extends StreamAction
case class RedisPub(channel: String, text: String, pattern: Option[String] = None) extends StreamAction

/** Outbound actions */
case class Matchmaking(check: Boolean) extends StreamAction
case class GameCreate(gameId: UUID) extends StreamAction
case class GameChangeSelection(gameId: UUID)  extends StreamAction
case class Pong(at: Long) extends StreamAction
case class RedisRaw(text: String) extends StreamAction
