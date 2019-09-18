package monarchy.marshalling.game

import org.scalatest.{Matchers, WordSpec}
import monarchy.game._
import monarchy.util.Json

class GameJsonBijectionSpec extends WordSpec with Matchers {

  object StubRandom extends scala.util.Random(5)

  val game = GameBuilder(
    seed = 5,
    players = Seq(
      Player(PlayerId(2L), Seq(
        Vec(4, 4) -> Knight,
        Vec(3, 4) -> Witch
      )),
      Player(PlayerId(3L), Seq(
        Vec(4, 5) -> Scout
      ))
    )
  )

  "GameJsonBijection" should {
    "correctly round-trip game with selection" in {
      val Accept(game2) = game.tileSelect(PlayerId(2L), Vec(4, 4))
      val to = GameJson.stringify(game2)
      val from = GameJson.parse[Game](to)
      assert(game2.copy(rand = StubRandom) == from.copy(rand = StubRandom))
    }

    "correctly round-trip game with selection (again)" in {
      val Accept(game2) = game.tileSelect(PlayerId(2L), Vec(4, 4))
      val to = GameJson.stringify(game2)
      val from = GameJson.parse[Game](to)
      assert(game2.copy(rand = StubRandom) == from.copy(rand = StubRandom))
    }
  }

}
