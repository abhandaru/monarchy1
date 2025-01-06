package monarchy.dal

import java.time.Instant
import java.util.UUID

sealed class Membership(val id: Int) extends EnumColumn
object Membership extends EnumColumnDef[Membership] {
  case object Admin extends Membership(0)
  case object Basic extends Membership(1)
}

case class User(
  id: UUID = NewId,
  secret: String,
  username: String,
  email: String,
  phoneNumber: Option[String] = None,
  rating: Int = 1000,
  membership: Membership = Membership.Basic,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object User extends TableSchema[User, UserTable](TableQuery[UserTable])

class UserTable(tag: Tag) extends TableDef[User](tag, "users") {
  def secret = column[String]("secret")
  def username = column[String]("username")
  def email = column[String]("email")
  def phoneNumber = column[Option[String]]("phone_number")
  def rating = column[Int]("rating")
  def membership = column[Membership]("membership")
  def * = (id, secret, username, email, phoneNumber, rating, membership, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply)
}
