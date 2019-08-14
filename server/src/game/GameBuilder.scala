package monarchy.game

import scala.util.Random

object GameBuilder {
  /**
   * NOTE: To support more players, we can just rotate a vector by
   * `2pi/n_players * i`. This probably will not make any sense for more than
   * 4 players given the taxicab geometry
   */
  val Directions = Array(Vec(1, 0), Vec(-1, 0))


  def apply(seed: Int, players: Seq[Player]): Game = {
    val rand = new Random(seed)
    val playersOrdered = rand.shuffle(players.sorted)
    val initialPieces = for {
      (player, i) <- playersOrdered.zipWithIndex
      playerDir = Directions(i)
      (p, pieceConf) <- player.formation
    } yield PieceLocation(p, PieceBuilder(pieceConf, player.id, playerDir))
    Game(
      rand = rand,
      players = playersOrdered,
      board = Board.Standard.commit(initialPieces),
      turns = Seq(Turn())
    )
  }
}
