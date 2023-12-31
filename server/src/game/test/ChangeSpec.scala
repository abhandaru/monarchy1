package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ChangeSpec extends AnyWordSpec with Matchers {

  "Change" should {
    "correctly apply `map` for [[Accept]]" in {
      val r = Accept(1).map(_ + 2)
      assert(r == Accept(3))
    }

    "correctly apply `map` for [[Reject]]" in {
      val r = (Reject.CannotAttack: Change[Int]).map(_ + 2)
      assert(r == Reject.CannotAttack)
    }

    "correctly apply `flatMap` for [[Accept]]" in {
      val r = Accept(1).flatMap { s => Accept(s + 2) }
      assert(r == Accept(3))
    }

    "correctly apply `flatMap` for [[Accept]] to [[Reject]]" in {
      val r = Accept(1).flatMap { _ => Reject.CannotAttack }
      assert(r == Reject.CannotAttack)
    }

    "correctly apply `flatMap` for [[Reject]]" in {
      val r = (Reject.CannotAttack: Change[Int]).flatMap { s => Accept(s + 2) }
      assert(r == Reject.CannotAttack)
    }
  }

}
