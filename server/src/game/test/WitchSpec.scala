package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WitchSpec extends AnyWordSpec with Matchers {
  import PlanarTooling.PlanarStringOps

  "Witch" should {

    "correctly generate attack patterns" in {
      val patterns = Witch.attackPatterns.pointSets(Vec(4, 4))
      assert(patterns contains PlanarTooling.points(
        """
        |████░████
        |████░████
        |████░████
        |████░████
        |█████████
        |█████████
        |█████████
        |█████████
        |█████████
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |█████████
        |█████████
        |█████████
        |█████████
        |█████░░░░
        |█████████
        |█████████
        |█████████
        |█████████
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |█████████
        |█████████
        |█████████
        |█████████
        |█████████
        |████░████
        |████░████
        |████░████
        |████░████
        """.plane
      ))
      assert(patterns contains PlanarTooling.points(
        """
        |█████████
        |█████████
        |█████████
        |█████████
        |░░░░█████
        |█████████
        |█████████
        |█████████
        |█████████
        """.plane
      ))
    }

    "correctly generate attack effects for left-pattern" in {
      val pointPattern = PointPattern(PlanarTooling.points(
        """
        |█████████
        |█████████
        |█████████
        |█████████
        |░░░░█████
        |█████████
        |█████████
        |█████████
        |█████████
        """.origin(4, 4)
      ))
      val effects = Witch.effectArea(Vec(4, 4), pointPattern)
      assert(PlanarTooling.compare(
        effects,
        """
        |█████████
        |█████████
        |█████████
        |█████████
        |░░░░█████
        |█████████
        |█████████
        |█████████
        |█████████
        """
      ))
    }
  }

}
