package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class KnightSpec extends AnyWordSpec with Matchers {
  import PlanarTooling.PlanarStringOps

  "Knight" should {
    "correctly generate movement pattern" in {
      assert(PlanarTooling.compare(
        Knight.movement(Vec(3, 3)),
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

    "correctly generate attack patterns" in {
      val patterns = Knight.attackPatterns.pointSets(Vec(1, 1))
      assert(patterns contains PlanarTooling.points(
        """
        |█░█
        |███
        |███
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |███
        |██░
        |███
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |███
        |███
        |█░█
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |███
        |░██
        |███
        """.plane
      ))
    }
  }

}
