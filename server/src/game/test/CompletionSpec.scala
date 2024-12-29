package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import monarchy.testutil.Ids

class CompletionSpec extends AnyWordSpec with Matchers {
  import Completion._
  import Player.Status._
  
  val seed = 44
  val p1 = PlayerId(Ids.A)
  val p2 = PlayerId(Ids.B)

  val game2Pieces = GameBuilder(
    seed = seed,
    players = Seq(
      Player(p1, Seq((Vec(7, 7), Assassin))),
      Player(p2, Seq((Vec(4, 6), Knight))),
    )
  )

  "Completion" should {
    "give incomplete for new game" in {
      assert(Completion(game2Pieces) === Incomplete)
    }

    "give complete with a drawn result for game with no pieces" in {
      val game = GameBuilder(seed, Seq(Player(p1, Nil), Player(p2, Nil)))
      assert(Completion(game) === Complete(
        gameStatus = Game.Status.Complete,
        playerStatuses = Map(p1 -> Drawn, p2 -> Drawn),
      ))
    }

    "give complete with win-lose result for game with 1 remaining piece" in {
      val game = GameBuilder(seed, Seq(Player(p1, Nil), Player(p2, Seq((Vec(4, 6), Knight)))))
      assert(Completion(game) === Complete(
        gameStatus = Game.Status.Complete,
        playerStatuses = Map(p1 -> Lost, p2 -> Won),
      ))
    }

    def passTurns(game: Game, n: Int): Change[Game] = {
      val playerIds = game.players.map(_.id).toArray
      val sequence = (0 until n).map { i => playerIds(i % playerIds.size) }
      val intermediate = sequence
        .foldLeft(Change(game2Pieces)) { case (c, id) => c.flatMap(_.commitTurn(id)) }
      // This method is reliable except that the next turn is always appended.
      // In practice, `Completion` will be call within `commitTurn` before that
      // is appended.
      intermediate.map { g => g.copy(turns = g.turns.drop(1)) }
    }

    "give incomplete for game with a few passed turns" in {
      val Accept(game) = passTurns(game2Pieces, 3)
      assert(Completion(game) === Incomplete)
    }

    "give complete with drawn result for game with too many passed turns" in {
      val Accept(game) = passTurns(game2Pieces, 6)
      assert(Completion(game) === Complete(
        gameStatus = Game.Status.Complete,
        playerStatuses = Map(p1 -> Drawn, p2 -> Drawn),
      ))
    }
  }

}
