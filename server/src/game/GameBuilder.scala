package monarchy.game

import java.util.UUID
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
  val Rotation = Matrix.Rotate180

  /** For now we always use the standard 11 x 11 [[Board]]. */
  val BoardSelection = Board.Standard

  def apply(seed: Int, players: Seq[Player]): Game = {
    val rand = new Random(seed)
    val (_, maxPt) = BoardSelection.boundingBox
    val playersOrdered = rand.shuffle(players)
    val piecesAdditions = for {
      (player, i) <- playersOrdered.zipWithIndex
      rotation = Rotation ^ i
      (pt, pieceConf) <- player.formation
    } yield {
      val ptRotated = (rotation * (pt - (maxPt / 2))) + (maxPt / 2);
      val dirRotated = rotation * Vec.I
      val pieceId = mkPieceId(ptRotated)
      val piece = PieceBuilder(pieceId, pieceConf, player.id, dirRotated);
      val pAdd = PieceAdd(ptRotated, piece)
      pAdd.copy(
        piece = pAdd.piece.copy(
          currentWait = initialWait(pAdd.piece.conf, i)
        )
      )
    }
    Game(
      rand = rand,
      players = playersOrdered,
      board = BoardSelection.commitAggregation(piecesAdditions),
      turns = Seq(Turn())
    )
  }

  private def mkPieceId(pt: Vec): PieceId = {
    val uuid = UUID.nameUUIDFromBytes(pt.hashCode.toString.getBytes)
    PieceId(uuid)
  }
}
