package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class DeltasSpec extends WordSpec with Matchers {
  import PlanarTooling.PlanarStringOps

  "Deltas" should {
    "correctly generate `rangeDeltas` for 2" in {
      assert(PlanarTooling.compare(
        Deltas.rangeDeltas(2),
        """
        |██░██
        |█░░░█
        |░░░░░
        |█░░░█
        |██░██
        """.origin(2, 2)
      ))
    }

    "correctly generate `rangeDeltas` for 3" in {
      assert(PlanarTooling.compare(
        Deltas.rangeDeltas(3),
        """
        |███░███
        |██░░░██
        |█░░░░░█
        |░░░░░░░
        |█░░░░░█
        |██░░░██
        |███░███
        """.origin(3, 3)
      ))
    }

    "correctly generate `rangeDeltasNoCenter` for 4" in {
      assert(PlanarTooling.compare(
        Deltas.rangeDeltasNoCenter(4),
        """
        |████░████
        |███░░░███
        |██░░░░░██
        |█░░░░░░░█
        |░░░░█░░░░
        |█░░░░░░░█
        |██░░░░░██
        |███░░░███
        |████░████
        """.origin(4, 4)
      ))
    }
  }

}
