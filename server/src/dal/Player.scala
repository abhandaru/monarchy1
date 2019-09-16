package monarchy.dal

import java.time.Instant

sealed class PlayerStatus(val id: Int) extends EnumColumn
object PlayerStatus extends EnumColumnDef[PlayerStatus] {
  case object Pending extends PlayerStatus(0)
  case object Won extends PlayerStatus(1)
  case object Lost extends PlayerStatus(2)
}

case class Player(
  id: Long = NewId,
  userId: Long,
  gameId: Long,
  status: PlayerStatus,
  rating: Int,
  ratingDelta: Option[Int] = None,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object Player extends TableSchema(TableQuery[PlayerTable])

class PlayerTable(tag: Tag) extends TableDef[Player](tag, "players") {
  def userId = column[Long]("user_id")
  def gameId = column[Long]("game_id")
  def status = column[PlayerStatus]("status")
  def rating = column[Int]("rating")
  def ratingDelta = column[Option[Int]]("rating_delta")
  def * = (id, userId, gameId, status, rating, ratingDelta, createdAt, updatedAt) <> ((Player.apply _).tupled, Player.unapply)
}
