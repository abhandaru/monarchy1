package monarchy.game

import scala.util.Random

object GameGenerator {
  def apply(seed: Int, players: Seq[Player]): Game = {
    val rand = new Random(seed)
    val initialPieces = for {
      player <- players
      (p, pieceConf) <- player.formation
    } yield (p, PieceGenerator(pieceConf, player.id, player.direction))
    Game(
      rand = rand,
      players = rand.shuffle(players.sorted),
      board = initialPieces.foldLeft(Board.Standard) { case (b, (v, p)) =>
        b.placePiece(v, p)
      },
      turns = Seq(Turn())
    )
  }
}
