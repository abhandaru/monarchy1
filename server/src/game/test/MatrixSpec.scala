package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class MatrixSpec extends WordSpec with Matchers {

  "Matrix" should {
    "correctly return original [[Matrix]] for `Identity` mult" in {
      val mat = Matrix(1, 2, 3, 4)
      assert(Matrix.Identity * mat == mat)
    }

    "correctly rotate I vector by 90° counter-clockwise" in {
      assert(Matrix.Rotate90CC * Vec.I == Vec(0, 1))
    }

    "correctly rotate J vector by 180°" in {
      assert(Matrix.Rotate180 * Vec.J == Vec(0, -1))
    }

    "correctly rotate J vector by 360°" in {
      assert(Matrix.Rotate180 * Matrix.Rotate180 * Vec.J == Vec.J)
    }
  }

}
