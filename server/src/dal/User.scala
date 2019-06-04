package monarchy.dal

import java.time.Instant

case class User(
  id: Long = NewId,
  username: String,
  phoneNumber: String,
  secret: String,
  createdAt: Instant = Instant.now,
  updatedAt: Instant = Instant.now
)

object UserSchema {
  import PostgresProfile.Implicits._
  class Def(tag: Tag) extends TableDef[User](tag, "users") {
    def username = column[String]("username")
    def phoneNumber = column[String]("phone_number")
    def secret = column[String]("secret")
    def * = (id, username, phoneNumber, secret, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply)
  }
}
