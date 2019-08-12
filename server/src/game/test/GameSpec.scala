package monarchy.game

import org.scalatest.{Matchers, WordSpec}

class GameSpec extends WordSpec with Matchers {
  import PlanarTooling.PlanarStringOps

  val game = GameBuilder(
    seed = 77,
    players = Seq(
      Player(PlayerId(2L), Seq((Vec(3, 3), Knight))),
      Player(PlayerId(1L), Seq((Vec(7, 7), Assassin)))
    )
  )

  "Game" should {
    "correctly order players with seeded randoms" in {
      assert(game.currentPlayer.id == PlayerId(1L))
    }

    "have no current selection" in {
      assert(game.currentSelection.isEmpty)
    }

    "have no current piece" in {
      assert(game.currentPiece.isEmpty)
    }

    "have correct selections for player ID=1" in {
      assert(game.selections == Set(Vec(7, 7)))
    }

    "have no movements without a selection" in {
      assert(game.movements == Deltas.empty)
    }

    "reject selection from player ID=2" in {
      assert(game.tileSelect(PlayerId(2L), Vec(7, 7)) == Reject.ChangeOutOfTurn)
    }

    "accept selection from player ID=1" in {
      assert(game.tileSelect(PlayerId(1L), Vec(7, 7)).accepted)
    }

    "have correct current selection for (7, 7)" in {
      val Accept(nextGame) = game.tileSelect(PlayerId(1L), Vec(7, 7))
      assert(nextGame.currentSelection == Some(Vec(7, 7)))
    }

    "have correct current piece for (7, 7)" in {
      val Accept(nextGame) = game.tileSelect(PlayerId(1L), Vec(7, 7))
      val piece = PieceBuilder(Assassin, PlayerId(1), Vec(0, 1))
      assert(nextGame.currentPiece == Some(piece))
    }

    "allow for movement after tile selection" in {
      val Accept(nextGame) = game.tileSelect(PlayerId(1L), Vec(7, 7))
      assert(nextGame.currentTurn.canMove)
    }

    "have correct movements for current piece on (7, 7)" in {
      val Accept(nextGame) = game.tileSelect(PlayerId(1L), Vec(7, 7))
      assert(PlanarTooling.compare(
        nextGame.movements,
        """
        |██#######██
        |█#########█
        |###########
        |#######░###
        |######░░░##
        |#####░░░░░#
        |####░░░░░░░
        |###░░░░#░░░
        |####░░░░░░░
        |█####░░░░░█
        |██####░░░██
        """
      ))
    }

    "reject move selection from player ID=2" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(1L), Vec(7, 7))
        g2 <- g1.moveSelect(PlayerId(2L), Vec(7, 8))
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    "reject move selection from player ID=1 on opponent piece" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(1L), Vec(3, 3))
        g2 <- g1.moveSelect(PlayerId(1L), Vec(4, 5))
      } yield g2
      assert(change == Reject.PieceActionWithoutOwnership)
    }

    "reject move selection from player ID=1 with bad coordinates" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(1L), Vec(7, 7))
        g2 <- g1.moveSelect(PlayerId(1L), Vec(3, 3))
      } yield g2
      assert(change == Reject.IllegalMoveSelection)
    }

    /**
     * Tests below will inspect state for a single valid move by player 1.
     */
    val moveChange = for {
      g1 <- game.tileSelect(PlayerId(1L), Vec(7, 7))
      g2 <- g1.moveSelect(PlayerId(1L), Vec(6, 5))
    } yield g2

    "accept move selection from player ID=1 with good coordinates" in {
      assert(moveChange.accepted)
    }

    "have correct current selection after move" in {
      val Accept(nextGame) = moveChange
      assert(nextGame.currentSelection.get == Vec(6, 5))
    }

    "have correct current tile after move" in {
      val Accept(nextGame) = moveChange
      assert(nextGame.currentTile.get.point == Vec(6, 5))

    }

    "correctly apply piece movement after move" in {
      val Accept(nextGame) = moveChange
      val piece = PieceBuilder(Assassin, PlayerId(1), Vec(0, 1))
      assert(nextGame.board.tile(Vec(7, 7)).get.piece.isEmpty)
      assert(nextGame.board.tile(Vec(6, 5)).get.piece == Some(piece))
    }

    "have correct attack options for moved piece" in {
      val Accept(nextGame) = moveChange
      assert(PlanarTooling.compare(
        nextGame.attacks.flatten,
        """
        |██#######██
        |█#########█
        |###########
        |###########
        |###########
        |#####░#####
        |####░#░####
        |#####░#####
        |###########
        |█#########█
        |██#######██
        """.plane
      ))
    }

    "have correct effect previews for attack option" in {
      val Accept(nextGame) = moveChange
      assert(PlanarTooling.compare(
        nextGame.effects(Set(Vec(6, 4))),
        """
        |██#######██
        |█#########█
        |###########
        |###########
        |###########
        |#####░#####
        |####░#░####
        |#####░#####
        |###########
        |█#########█
        |██#######██
        """.plane
      ))
    }

    "reject attack selection from player ID=2" in {
      val change = for {
        g1 <- moveChange
        g2 <- g1.attackSelect(PlayerId(2L), Set(Vec(5, 5)))
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    "reject attack selection from player ID=1 with bad coordinates" in {
      val change = for {
        g1 <- moveChange
        g2 <- g1.attackSelect(PlayerId(1L), Set(Vec(5, 4)))
      } yield g2
      assert(change == Reject.IllegalAttackSelection)
    }
  }

}
