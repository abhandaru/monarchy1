package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {

  "Board$" should {
    "have correct Standard [[Board]]" in {
      assert(PlanarTooling.compare(
        Board.Standard.tiles,
        """
        |██░░░░░░░██
        |█░░░░░░░░░█
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |░░░░░░░░░░░
        |█░░░░░░░░░█
        |██░░░░░░░██
        """
      ))
    }
  }

  "Board" should {
    val knight = PieceBuilder(PieceId(1), Knight, PlayerId(1), Vec(1, 0))
    val shrub = PieceBuilder(PieceId(2), Shrub, PlayerId(1), Vec(1, 0))
    val board = BoardTooling.from(
      """
      |██░░░░░░░██
      |█░░░░░░░░░█
      |░░░░░░░░░░░
      |░░░0░░░░░░░
      |░░░░░░░░░░░
      |░░░░░░░░░░░
      |░░░░░░░░░░░
      |░░░░░░░░░░░
      |░░░░░░░░░░░
      |█░░░░░░░░░█
      |██░░░░░░░██
      """,
      Seq(knight)
    )

    "return correct tile" in {
      assert(board.tile(Vec(0, 0)).isEmpty)
      assert(board.tile(Vec(2, 0)).nonEmpty)
      assert(board.piece(Vec(3, 3)).get.piece.conf == Knight)
    }

    "return pieces for correct player" in {
      assert(board.pieces(PlayerId(1)).nonEmpty)
      assert(board.pieces(PlayerId(2)).isEmpty)
    }

    "return correctly moved piece" in {
      val nextBoard = board.move(Vec(3, 3), Vec(5, 4))
      assert(nextBoard.piece(Vec(3, 3)).isEmpty)
      assert(nextBoard.piece(Vec(5, 4)).get.piece.conf == Knight)
    }

    "return correctly updated piece" in {
      val nextBoard = board.commit(PieceUpdate(Vec(3, 3), _.copy(currentHealth = 33)))
      assert(nextBoard.piece(Vec(3, 3)).get.piece.currentHealth == 33)
    }

    "return correctly aggregates tile changes" in {
      val nextBoard = board.commitAggregation(Seq(
        PieceAdd(Vec(6, 7), shrub),
        PieceUpdate(Vec(3, 3), _.copy(currentWait = 1))
      ))
      assert(nextBoard.piece(Vec(3, 3)).get.piece.currentWait == 1)
      assert(nextBoard.piece(Vec(6, 7)).get.piece.conf == Shrub)
    }
  }

}
