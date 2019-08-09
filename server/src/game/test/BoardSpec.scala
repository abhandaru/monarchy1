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
    val knight = PieceGenerator(Knight, PlayerId(1), Vec(1, 0))
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
      assert(board.tile(Vec(3, 3)).get.piece.get == knight)
    }

    "return pieces for correct player" in {
      assert(board.pieces(PlayerId(1)).nonEmpty)
      assert(board.pieces(PlayerId(2)).isEmpty)
    }

    "return correctly moved piece" in {
      val nextBoard = board.move(Vec(3, 3), Vec(5, 4))
      assert(nextBoard.tile(Vec(3, 3)).get.piece.isEmpty)
      assert(nextBoard.tile(Vec(5, 4)).get.piece.get == knight)
    }
  }

}
