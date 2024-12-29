package monarchy.game

sealed trait Completion

object Completion {
  case object Incomplete extends Completion
  case class Complete(
      gameStatus: Game.Status,
      playerStatuses: Map[PlayerId, Player.Status]
  ) extends Completion

  val MaxRoundsPassed = 3

  def apply(game: Game): Completion = {
    checkForfeit(game)
      .orElse { checkDrawByPass(game) }
      .orElse { checkBoard(game) }
      .getOrElse( Incomplete )
  }

  private def checkForfeit(game: Game): Option[Completion] = {
    if (game.currentTurn.forfeit) {
      val loserId = game.currentPlayer.id 
      val winnerId = game.players.filter(_.id != loserId).head.id
      Some(mkWinLose(winnerId, loserId))
    } else None
  }

  // If the last `MaxRoundsPassed` rounds contained only turns where no actions
  // were taken, then we end the game in a draw. Later we can expand this to
  // cover scenarios where no damage is done, and max turns.
  private def checkDrawByPass(game: Game): Option[Completion] = {
    val playerCount = game.players.size
    val turnCount = MaxRoundsPassed * playerCount
    val turns = game.turns.take(turnCount)
    if (turns.size < turnCount) None
    else if (turns.exists(!_.passed)) None
    else Some(mkDraw(game.players.map(_.id)))
  }

  private def checkBoard(game: Game): Option[Completion] = {
    val (alive, dead) = game.players.partition { player =>
      val pieces = game.board.pieces(player.id)
      pieces.exists(_.piece.conf.living)
    }
    if (alive.isEmpty) Some(mkDraw(game.players.map(_.id)))
    else if (alive.size == 1) Some(mkWinLose(alive.head.id, dead.head.id))
    else None
  }

  private def mkWinLose(winnerId: PlayerId, loserId: PlayerId): Complete = {
    Complete(Game.Status.Complete, Map(
      loserId -> Player.Status.Lost,
      winnerId -> Player.Status.Won,
    ))
  }

  private def mkDraw(playerIds: Seq[PlayerId]): Complete = {
    val assoc = playerIds.map(_ -> Player.Status.Drawn).toMap
    Complete(Game.Status.Complete, assoc)
  }
}