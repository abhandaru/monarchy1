package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import monarchy.testutil.Ids

class MovementSpec extends AnyWordSpec with Matchers {
  val knight = PieceBuilder(PieceId(Ids.A), Knight, PlayerId(Ids.A), Vec(1, 0))
  val knightLoc = PieceLocation(Vec(3, 3), knight)
  val b0 = Board.Standard.commitAggregation(Seq(
    PieceAdd(Vec(3, 3), knight),
  ))

  "Movement" should {
    "yield proper tiles for Knight close to edge" in {
      val v0 = Vec(3, 3)
      val deltas = Movement.reachablePoints(b0, knightLoc)
      assert(PlanarTooling.compare(
       deltas,
        """
        |███░███
        |██░░░██
        |█░░░░░█
        |░░░█░░░
        |█░░░░░█
        |██░░░██
        |███░███
        """
      ))
    }

    "yield proper tiles for Knight with friendly 2 tiles right" in {
      val knight2 = knight.copy(id = PieceId(Ids.B))
      val board2 = b0.commitAggregation(Seq(PieceAdd(Vec(3, 5), knight2)))
      val deltas = Movement.reachablePoints(board2, knightLoc)
      assert(PlanarTooling.compare(
       deltas,
        """
        |███░███
        |██░░░██
        |█░░░░░█
        |░░░█░█░
        |█░░░░░█
        |██░░░██
        |███░███
        """
      ))
    }

    "yield proper tiles for Knight with enemy two tiles below" in {
      val knight2 = knight.copy(id = PieceId(Ids.B), playerId = PlayerId(Ids.B))
      val board2 = b0.commitAggregation(Seq(PieceAdd(Vec(5, 3), knight2)))
      val deltas = Movement.reachablePoints(board2, knightLoc)
      assert(PlanarTooling.compare(
       deltas,
        """
        |███░███
        |██░░░██
        |█░░░░░█
        |░░░█░░░
        |█░░░░░█
        |██░█░██
        |███████
        """
      ))
    }

    "yield proper tiles for Knight with enemy 1 tile above" in {
      val pyro = PieceBuilder(PieceId(Ids.B), Pyromancer, PlayerId(Ids.B), Vec(1, 0))
      val board2 = b0.commitAggregation(Seq(PieceAdd(Vec(2, 3), pyro)))
      val deltas = Movement.reachablePoints(board2, knightLoc)
      assert(PlanarTooling.compare(
       deltas,
        """
        |███████
        |██░█░██
        |█░░█░░█
        |░░░█░░░
        |█░░░░░█
        |██░░░██
        |███░███
        """
      ))
    }
  }
}
