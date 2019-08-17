package monarchy.game

import scala.util.Random

object GameBuilder {
  /**
   * Certain pieces (or classes of pieces) are not permitted to attack on the
   * first turn of the game. This improves game play and discourages rush
   * tactics (at least a little bit).
   */
  val InitialWaitPieces: Set[PieceConf] =
    Set(Witch, Pyromancer, Scout, MudGolem)

  def initialWait(conf: PieceConf, index: Int): Int = {
    if (index == 0 && InitialWaitPieces(conf)) 1 else 0
  }

  /**
   * NOTE: To support more players, we can just rotate a vector by
   * `2pi/n_players * i`. This probably will not make any sense for more than
   * 4 players given the taxicab geometry
   */
  val Directions = Array(Vec(1, 0), Vec(-1, 0))

  def apply(seed: Int, players: Seq[Player]): Game = {
    val rand = new Random(seed)
    val playersOrdered = rand.shuffle(players.sorted)
    val piecesAdditions = for {
      (player, i) <- playersOrdered.zipWithIndex
      playerDir = Directions(i)
      (p, pieceConf) <- player.formation
    } yield {
      val pieceId = PieceId(p.hashCode)
      val pl = PieceAdd(p, PieceBuilder(pieceId, pieceConf, player.id, playerDir))
      pl.copy(
        piece = pl.piece.copy(
          currentWait = initialWait(pl.piece.conf, i)
        )
      )
    }
    Game(
      rand = rand,
      players = playersOrdered,
      board = Board.Standard.commitAggregation(piecesAdditions),
      turns = Seq(Turn())
    )
  }
}
