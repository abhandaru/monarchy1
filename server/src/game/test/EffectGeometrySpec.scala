package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class EffectGeometrySpec extends WordSpec with Matchers {
  import EffectGeometry._

  "EffectGeometry.directionSnap" should {
    "give `NoRotation` for same dir" in {
      assert(directionSnap(Vec(1, 0), Vec(2, 0)) == NoRotation(Vec(1, 0)))
    }

    "give `NoRotation` for forward diagonal dir" in {
      assert(directionSnap(Vec(0, 1), Vec(1, 1)) == NoRotation(Vec(0, 1)))
    }

    "give `HalfRotation` for opposite dir" in {
      assert(directionSnap(Vec(1, 0), Vec(-2, 0)) == HalfRotation(Vec(-1, 0)))
    }

    "give `HalfRotation` for backwards diagonal dir" in {
      assert(directionSnap(Vec(-1, 0), Vec(2, 2)) == HalfRotation(Vec(1, 0)))
    }

    "give `Clockwise90` for orthogonal right dir" in {
      assert(directionSnap(Vec(1, 0), Vec(0, -1)) == Clockwise90(Vec(0, -1)))
    }

    "give `Clockwise90` for right dir" in {
      assert(directionSnap(Vec(0, -1), Vec(-2, -1)) == Clockwise90(Vec(-1, 0)))
    }

    "give `CounterClockwise90` for orthogonal left dir" in {
      assert(directionSnap(Vec(0, 1), Vec(-2, 0)) == CounterClockwise90(Vec(-1, 0)))
    }

    "not crash for zero vector" in {
      directionSnap(Vec(0, -1), Vec(0, 0))
    }
  }

}
