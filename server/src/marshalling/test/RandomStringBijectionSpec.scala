package monarchy.marshalling

import org.scalatest.{Matchers, WordSpec}
import scala.util.Random

class RandomStringBijectionSpec extends WordSpec with Matchers {

  "RandomStringBijection" should {
    "correctly serialize random with internal state" in {
      val ctrl = new Random(8)
      val test = RandomStringBijection.invert(RandomStringBijection(new Random(8)))

      assert(test.nextInt == ctrl.nextInt)
      assert(test.nextLong == ctrl.nextLong)
    }
  }

}
