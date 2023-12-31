package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EffectGeometrySpec extends AnyWordSpec with Matchers {
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

  "EffectGeometry.pointsAlongSegment" should {
    "give all tiles on the diagonal" in {
      val points = pointsAlongSegment(Vec(0, 0), Vec(3, 3))
      assert(points == Seq(Vec(1, 1), Vec(2, 2), Vec(3, 3)))
    }

    "give all tiles on the negative diagonal" in {
      val points = pointsAlongSegment(Vec(0, 3), Vec(3, 0))
      assert(points == Seq(Vec(1, 2), Vec(2, 1), Vec(3, 0)))
    }

    "gives one tile for knight-move line" in {
      val points = pointsAlongSegment(Vec(2, 0), Vec(0, 1))
      assert(points == Seq(Vec(0, 1)))
    }

    "gives two tiles for double knight-move line" in {
      val points = pointsAlongSegment(Vec(4, 2), Vec(0, 0))
      assert(points == Seq(Vec(2, 1), Vec(0, 0)))
    }

    "gives two tiles for 1-5 off-horizontal line" in {
      val points = pointsAlongSegment(Vec(0, 0), Vec(1, 5))
      assert(points == Seq(Vec(0, 1), Vec(1, 4), Vec(1, 5)))
    }

    "gives two tiles for 4-1 off-horizontal line" in {
      val points = pointsAlongSegment(Vec(1, 4), Vec(0, 0))
      assert(points == Seq(Vec(1, 3), Vec(0, 1), Vec(0, 0)))
    }

    "gives three tiles for 3-2 diagonal line" in {
      val points = pointsAlongSegment(Vec(0, 3), Vec(2, 0))
      assert(points == Seq(Vec(1, 2), Vec(1, 1), Vec(2, 0)))
    }
  }
}
