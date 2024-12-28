package monarchy.dal

import java.time.Instant
import java.util.UUID

case class Profile(
  id: UUID = NewId,
  userId: UUID,
  avatar: String,
  color: String,
  colorAccent: Option[String] = None,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object Profile extends TableSchema[Profile, ProfileTable](TableQuery[ProfileTable])

class ProfileTable(tag: Tag) extends TableDef[Profile](tag, "profiles") {
  def userId = column[UUID]("user_id")
  def avatar = column[String]("avatar")
  def color = column[String]("color")
  def colorAccent = column[Option[String]]("color_accent")
  def * = (id, userId, avatar, color, colorAccent, createdAt, updatedAt) <> ((Profile.apply _).tupled, Profile.unapply)
}
