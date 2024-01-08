package monarchy.marshalling.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import monarchy.game._
import monarchy.util.Json
import monarchy.testutil.Ids

class GameJsonBijectionSpec extends AnyWordSpec with Matchers {

  object StubRandom extends scala.util.Random(5)

  val game = GameBuilder(
    seed = 5,
    players = Seq(
      Player(PlayerId(Ids.B), Seq(
        Vec(4, 4) -> Knight,
        Vec(3, 4) -> Witch
      )),
      Player(PlayerId(Ids.C), Seq(
        Vec(4, 5) -> Scout
      ))
    )
  )

  "GameJsonBijection" should {
    "correctly round-trip game with selection" in {
      val Accept(game2) = game.tileSelect(PlayerId(Ids.B), Vec(4, 4))
      val to = GameJson.stringify(game2)
      val from = GameJson.parse[Game](to)
      assert(game2.copy(rand = StubRandom) == from.copy(rand = StubRandom))
    }

    "correctly round-trip game with selection (again)" in {
      val Accept(game2) = game.tileSelect(PlayerId(Ids.B), Vec(4, 4))
      val to = GameJson.stringify(game2)
      val from = GameJson.parse[Game](to)
      assert(game2.copy(rand = StubRandom) == from.copy(rand = StubRandom))
    }
  }

}
