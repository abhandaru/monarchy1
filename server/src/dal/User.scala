package monarchy.dal

import java.time.Instant

case class User(
  id: Long = NewId,
  username: String,
  phoneNumber: String,
  rating: Int = 1000,
  secret: String,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

import PostgresProfile.Implicits._
object User extends TableSchema(TableQuery[UserTable])

class UserTable(tag: Tag) extends TableDef[User](tag, "users") {
  def username = column[String]("username")
  def phoneNumber = column[String]("phone_number")
  def rating = column[Int]("rating")
  def secret = column[String]("secret")
  def * = (id, username, phoneNumber, rating, secret, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply)
}
