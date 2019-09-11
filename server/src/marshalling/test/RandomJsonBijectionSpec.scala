package monarchy.marshalling

import org.scalatest.{Matchers, WordSpec}
import scala.util.Random

class RandomJsonBijectionSpec extends WordSpec with Matchers {

  "RandomJsonBijection" should {
    "correctly serialize random with internal state" in {
      val ctrl = new Random(8)
      val test = RandomJsonBijection.invert(RandomJsonBijection(new Random(8)))

      assert(test.nextInt == ctrl.nextInt)
      assert(test.nextLong == ctrl.nextLong)
    }
  }

}
