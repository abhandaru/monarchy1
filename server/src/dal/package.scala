package monarchy

package object dal {
  import PostgresProfile.Implicits._

  val NewId: Long = 0L
  val UserQuery = TableQuery[UserSchema.Def]
}
