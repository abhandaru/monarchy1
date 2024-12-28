package monarchy.dal

import java.time.Instant
import java.util.UUID

sealed class PlayerStatus(val id: Int) extends EnumColumn
object PlayerStatus extends EnumColumnDef[PlayerStatus] {
  case object Pending extends PlayerStatus(0)
  case object Won extends PlayerStatus(1)
  case object Lost extends PlayerStatus(2)
}

sealed class PlayerRole(val id: Int) extends EnumColumn
object PlayerRole extends EnumColumnDef[PlayerRole] {
  case object Competitor extends PlayerRole(0)
  case object Spectator extends PlayerRole(1)
}

case class Player(
  id: UUID = NewId,
  userId: UUID,
  gameId: UUID,
  status: PlayerStatus,
  role: PlayerRole,
  rating: Int,
  ratingDelta: Option[Int] = None,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object Player extends TableSchema[Player, PlayerTable](TableQuery[PlayerTable])

class PlayerTable(tag: Tag) extends TableDef[Player](tag, "players") {
  def userId = column[UUID]("user_id")
  def gameId = column[UUID]("game_id")
  def status = column[PlayerStatus]("status")
  def role = column[PlayerRole]("role")
  def rating = column[Int]("rating")
  def ratingDelta = column[Option[Int]]("rating_delta")
  def * = (id, userId, gameId, status, role, rating, ratingDelta, createdAt, updatedAt) <> ((Player.apply _).tupled, Player.unapply)
}
