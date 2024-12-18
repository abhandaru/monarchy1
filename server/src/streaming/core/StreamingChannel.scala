package monarchy.streaming.core

import java.util.UUID

object StreamingChannel {
  def base(suffix: String) = s"monarchy/streaming/$suffix"
  final val Public = base("public")
  final val Matchmaking = base("matchmaking")

  def personalBase(userId: UUID, suffix: String) = base(s"personal/$userId/$suffix")
  def gameBase(userId: UUID, suffix: String) = personalBase(userId, s"game/$suffix")
  def gameCreate(userId: UUID) = gameBase(userId, "create")
  def gameSelectTile(userId: UUID) = gameBase(userId, "selectTile")
  def gameMove(userId: UUID) = gameBase(userId, "move")
  def gameAttack(userId: UUID) = gameBase(userId, "attack")
  def gameDirection(userId: UUID) = gameBase(userId, "direction")
  
  def gamePattern(userId: UUID) = gameBase(userId, "*")
}
