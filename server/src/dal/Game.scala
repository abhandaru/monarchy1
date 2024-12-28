package monarchy.dal

import java.time.Instant
import java.util.UUID

sealed class GameStatus(val id: Int) extends EnumColumn
object GameStatus extends EnumColumnDef[GameStatus] {
  case object Started extends GameStatus(0)
  case object Complete extends GameStatus(1)
}

case class Game(
  id: UUID = NewId,
  seed: Int,
  status: GameStatus,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object Game extends TableSchema[Game, GameTable](TableQuery[GameTable])

class GameTable(tag: Tag) extends TableDef[Game](tag, "games") {
  def seed = column[Int]("seed")
  def status = column[GameStatus]("status")
  def * = (id, seed, status, createdAt, updatedAt) <> ((Game.apply _).tupled, Game.unapply)
}
