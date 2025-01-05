package monarchy.graphql

import monarchy.game.{PlayerId, Player => GamePlayer}

object Rating {
  import GamePlayer.Status

  // Simple struct to package results.
  case class Player(id: PlayerId, status: Status, rating: Int)
  case class Rating(prev: Int, delta: Int) {
    def next: Int = prev + delta
  }

  /**
   * Get all the player pairs (order matters) and compute the pair-wise Elo
   * deltas. For now, sum up the deltas per player (vs. average). If these
   * swings are too large in multiplayer formats, we can do something more
   * sophisticated.
   * 
   * NOTE: We repeat the symmetrical ELO calculation so that later we can 
   * introduce a different K-factor for new or experienced players.
   */
  def compute(players: Seq[Player]): Map[PlayerId, Rating] = {
    players.map { p0 =>
      val opponents = players.filter(_.id != p0.id)
      val deltaSum = opponents.map { p =>
        val actual = computeActualScore(p0, p)
        computeEloDelta(p0.rating, p.rating, actual)
      }.sum
      p0.id -> Rating(p0.rating, deltaSum)
    }.toMap
  }

  private def computeActualScore(p0: Player, p1: Player): Double = {
    (p0.status, p1.status) match {
      case (Status.Won, Status.Lost) => 1.0
      case (Status.Lost, Status.Won) => 0.0
      case _ => 0.5
    }
  }

  /**
   * ELO Rating System calculation based on the chess rating system developed by Arpad Elo.
   * Formula: Rn = Ro + K * (S - Se), where:
   *
   *   Rn = new rating
   *   Ro = old rating
   *   K = weight constant (32 is common for players under 2100)
   *   S = actual score (1 for win, 0 for loss)
   *   Se = expected score = 1 / (1 + 10^((opponent_rating - player_rating)/400))
   * 
   * Reference: https://en.wikipedia.org/wiki/Elo_rating_system
   */
  private def computeEloDelta(eloA: Int, eloB: Int, actual: Double): Int = {
    val k = 32 // K-factor determines how much ratings can change
    val expected = 1.0 / (1.0 + Math.pow(10, (eloA - eloB) / 400.0))
    Math.round(k * (actual - expected)).toInt
  }
}
