package monarchy.game

object PieceBuilder {
  def apply(
    conf: PieceConf,
    playerId: PlayerId,
    playerDir: Vec
  ): Piece = Piece(
    conf = conf,
    playerId = playerId,
    currentHealth = conf.maxHealth,
    currentWait = math.floor(conf.maxWait / 2).toInt,
    currentDirection = playerDir
  )
}

case class Piece(
  conf: PieceConf,
  playerId: PlayerId,
  currentHealth: Int,
  currentWait: Int,
  currentDirection: Vec,
  blockingAjustment: Double = 0.0,
  paralyzed: Boolean = false
) {
  def canAct = !paralyzed && currentWait == 0
}
