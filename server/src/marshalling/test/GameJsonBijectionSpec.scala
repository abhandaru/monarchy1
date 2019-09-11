package monarchy.marshalling

import org.scalatest.{Matchers, WordSpec}
import monarchy.game._
import monarchy.util.Json

class GameJsonBijectionSpec extends WordSpec with Matchers {

  val game = GameBuilder(
    seed = 5,
    players = Seq(
      Player(PlayerId(2L), Seq(
        Vec(4, 4) -> Knight
      )),
      Player(PlayerId(2L), Seq(
        Vec(4, 5) -> Scout
      ))
    )
  )

  "GameJsonBijection" should {
    "correctly serialize game" in {
      println(Json.stringify(game))
    }
  }

}
