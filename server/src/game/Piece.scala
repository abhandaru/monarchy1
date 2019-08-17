package monarchy.game

object PieceBuilder {
  def apply(
    id: PieceId,
    conf: PieceConf,
    playerId: PlayerId,
    playerDir: Vec
  ): Piece = Piece(
    id = id,
    conf = conf,
    playerId = playerId,
    currentHealth = conf.maxHealth,
    currentDirection = playerDir
  )
}

case class PieceId(id: Int) extends AnyVal

case class PieceEffect(casterId: PieceId, effect: Effect)

case class Piece(
  id: PieceId,
  conf: PieceConf,
  playerId: PlayerId,
  currentHealth: Int,
  currentWait: Int = 0,
  currentDirection: Vec,
  currentEffects: Seq[PieceEffect] = Nil,
  currentFocus: Boolean = false,
  blockingAjustment: Double = 0.0,
) {
  def paralyzed: Boolean = {
    currentEffects.map(_.effect).exists {
      case Paralyze(_) => true
      case _ => false
    }
  }

  def canAct: Boolean =
    !paralyzed && currentWait == 0

  def canBlock: Boolean =
    !paralyzed

  def currentBlocking: Double =
    conf.blocking + blockingAjustment
}
