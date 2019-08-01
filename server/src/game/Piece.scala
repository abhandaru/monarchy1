package monarchy.game

object PieceGenerator {
  def apply(
    conf: PieceConf,
    playerId: PlayerId,
    playerDir: Vec
  ): Piece = Piece(
    conf = conf,
    playerId = playerId,
    currentHealth = conf.maxHealth,
    currentWait = Math.floor(conf.maxWait / 2).toInt,
    currentDirection = playerDir
  )
}

case class Piece(
  conf: PieceConf,
  playerId: PlayerId,
  currentHealth: Int,
  currentWait: Int,
  currentDirection: Vec,
  blockingAjustment: Double = 0.0
)
