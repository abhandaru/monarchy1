package monarchy.game

case class PlayerContext(id: Long)

object PieceGenerator {
  def apply(
    conf: PieceConf,
    player: PlayerContext
  ): Piece = Piece(
    conf = conf,
    player = player,
    health = conf.maxHealth,
    _wait = Math.floor(conf.maxWait / 2).toInt
  )
}

case class Piece(
  conf: PieceConf,
  player: PlayerContext,
  health: Int,
  _wait: Int = 0,
  blockingAjustment: Double = 0.0
)
