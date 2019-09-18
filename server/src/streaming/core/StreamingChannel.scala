package monarchy.streaming.core

object StreamingChannel {
  def base(suffix: String) = s"monarchy/streaming/$suffix"
  final val Public = base("public")
  final val Matchmaking = base("matchmaking")

  def personalBase(userId: Long, suffix: String) = base(s"personal/$userId/$suffix")
  def gameBase(userId: Long, suffix: String) = personalBase(userId, s"game/$suffix")
  def gameCreate(userId: Long) = gameBase(userId, "create")
  def gameSelectTile(userId: Long) = gameBase(userId, "selectTile")

  def gamePattern(userId: Long) = gameBase(userId, "*")
}
