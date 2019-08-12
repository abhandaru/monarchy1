package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class WitchSpec extends WordSpec with Matchers {
  import PlanarTooling.PlanarStringOps

  "Witch" should {
    "correctly generate movement pattern" in {
      assert(PlanarTooling.compare(
        Witch.movement(Vec(3, 3)),
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
