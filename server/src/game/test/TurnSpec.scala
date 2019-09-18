package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class TurnSpec extends WordSpec with Matchers {
  val turn = Turn()

  "Turn" should {
    "correctly disallow piece actions without selection" in {
      assert(turn.canSelect)
      assert(turn.canDeselect)
      assert(!turn.canMove)
      assert(!turn.canAttack)
      assert(!turn.canDir)
    }

    "reject piece move action without selection" in {
      val change = turn.act(MoveSelect(Vec(2, 2)))
      assert(change == Reject.CannotMove)
    }

    "reject piece attack action without selection" in {
      val change = turn.act(AttackSelect(Set(Vec(2, 3))))
      assert(change == Reject.CannotAttack)
    }

    "reject piece dir action without selection" in {
      val change = turn.act(DirSelect(Vec.J))
      assert(change == Reject.CannotChangeDirection)
    }

    val turnWithSelection = turn.act(TileSelect(Vec(0, 0)))

    "correctly allow all actions with selection" in {
      val Accept(next) = turnWithSelection
      assert(next.canSelect)
      assert(next.canDeselect)
      assert(next.canMove)
      assert(next.canAttack)
      assert(next.canDir)
    }

    "return extracted point with selection" in {
      val Accept(next) = turnWithSelection
      assert(next.select.get == Vec(0, 0))
    }
  }

}
