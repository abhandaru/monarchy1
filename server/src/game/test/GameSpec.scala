package monarchy.game

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalactic.{Equality, TolerantNumerics}
import monarchy.testutil.Ids

class GameSpec extends AnyWordSpec with Matchers {
  import PlanarTooling.PlanarStringOps
  
  implicit val DoubleEq: Equality[Double] =
    TolerantNumerics.tolerantDoubleEquality(1e-3)

  val game = GameBuilder(
    seed = 77,
    players = Seq(
      // This player stays in the same place
      Player(PlayerId(Ids.A), Seq((Vec(7, 7), Assassin), (Vec(8, 6), Scout))),
      // This player gets rotated
      Player(PlayerId(Ids.B), Seq((Vec(4, 6), Knight), (Vec(5, 4), FrostGolem))),
    )
  )

  "Game" should {
    "correctly order players with seeded randoms" in {
      assert(game.currentPlayer.id == PlayerId(Ids.A))
    }

    "have no current selection" in {
      assert(game.currentSelection.isEmpty)
    }

    "have no current piece" in {
      assert(game.currentPiece.isEmpty)
    }

    "have correct selections for player ID=A" in {
      assert(game.selections == Set(Vec(7, 7), Vec(8, 6)))
    }

    "have no movements without a selection" in {
      assert(game.movements == Deltas.empty)
    }

    "have applied initial wait to scount" in {
      val Some(PieceLocation(p, piece)) = game.board.piece(Vec(8, 6))
      assert(piece.currentWait == 1)
    }

    "have applied no initial wait to frost golem" in {
      val Some(PieceLocation(p, piece)) = game.board.piece(Vec(5, 6))
      assert(piece.currentWait == 0)
    }

    "reject selection from player ID=B" in {
      assert(game.tileSelect(PlayerId(Ids.B), Vec(7, 7)) == Reject.ChangeOutOfTurn)
    }

    //
    // Tests below will inspect state for a single valid tile selection by player 1.
    //
    val selectionChange = game.tileSelect(PlayerId(Ids.A), Vec(7, 7))

    "accept selection from player ID=A" in {
      assert(selectionChange.accepted)
    }

    "have correct current selection for (7, 7)" in {
      val Accept(nextGame) = selectionChange
      assert(nextGame.currentSelection == Some(Vec(7, 7)))
    }

    "have correct current piece for (7, 7)" in {
      val Accept(nextGame) = selectionChange
      assert(nextGame.currentPiece.get.conf == Assassin)
    }

    "allow for movement after tile selection" in {
      val Accept(nextGame) = selectionChange
      assert(nextGame.currentTurn.canMove)
    }

    "have correct movements for current piece on (7, 7)" in {
      val Accept(nextGame) = selectionChange
      assert(PlanarTooling.compare(
        nextGame.movements,
        """
        |██#######██
        |█#########█
        |###########
        |#######░###
        |######░░░##
        |#####░█░░░#
        |####█░░░░░░
        |###░░░░█░░░
        |####░░█░░░░
        |█####░░░░░█
        |██####░░░██
        """
      ))
    }

    "reject move selection from player ID=2" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(Ids.A), Vec(7, 7))
        g2 <- g1.moveSelect(PlayerId(Ids.B), Vec(7, 8))
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    "reject move selection from player ID=A on opponent piece" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(Ids.A), Vec(6, 4))
        g2 <- g1.moveSelect(PlayerId(Ids.A), Vec(4, 5))
      } yield g2
      assert(change == Reject.PieceActionWithoutOwnership)
    }

    "reject move selection from player ID=A with bad coordinates" in {
      val change = for {
        g1 <- game.tileSelect(PlayerId(Ids.A), Vec(7, 7))
        g2 <- g1.moveSelect(PlayerId(Ids.A), Vec(6, 4))
      } yield g2
      assert(change == Reject.IllegalMoveSelection)
    }

    //
    // Tests below will inspect state for a single valid move by player 1.
    //
    val moveChange = for {
      g1 <- selectionChange
      g2 <- g1.moveSelect(PlayerId(Ids.A), Vec(6, 5))
    } yield g2

    "accept move selection from player ID=A with good coordinates" in {
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

    "have no available movements after move" in {
      val Accept(nextGame) = moveChange
      assert(nextGame.movements.isEmpty)
    }

    "correctly apply piece movement after move" in {
      val Accept(nextGame) = moveChange
      assert(nextGame.board.tile(Vec(7, 7)).get.piece.isEmpty)
      assert(nextGame.board.tile(Vec(6, 5)).get.piece.get.conf == Assassin)
    }

    "have all pieces be in the correct position" in {
      val Accept(nextGame) = moveChange
      assert(PlanarTooling.compare(
        nextGame.board.pieces,
        """
        |██#######██
        |█#########█
        |###########
        |###########
        |###########
        |######░####
        |####░░#####
        |###########
        |######░####
        |█#########█
        |██#######██
        """
      ))
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
        g2 <- g1.attackSelect(PlayerId(Ids.B), Set(Vec(5, 5)))
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    "reject attack selection from player ID=A with bad coordinates" in {
      val change = for {
        g1 <- moveChange
        g2 <- g1.attackSelect(PlayerId(Ids.A), Set(Vec(5, 4)))
      } yield g2
      assert(change == Reject.IllegalAttackSelection)
    }

    //
    // Tests below will inspect state for a valid attack by player 1.
    //
    val attackChange = for {
      g1 <- moveChange
      g2 <- g1.attackSelect(PlayerId(Ids.A), Set(Vec(6, 4)))
    } yield g2

    "accept attack selection from player ID=A with good coordinates" in {
      assert(attackChange.accepted)
    }

    "have correct current selection after attacking" in {
      val Accept(nextGame) = attackChange
      assert(nextGame.currentSelection.get == Vec(6, 5))
    }

    "have correct current tile after attacking" in {
      val Accept(nextGame) = attackChange
      assert(nextGame.currentTile.get.point == Vec(6, 5))
    }

    "have no available attacks after attacking" in {
      val Accept(nextGame) = attackChange
      assert(nextGame.attacks.isEmpty)
    }

    "have applied direction to attacker correctly" in {
      val Accept(nextGame) = attackChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 5))
      assert(piece.currentDirection == Vec(0, -1))
    }

    "have applied damage to defender correctly" in {
      val Accept(nextGame) = attackChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 4))
      assert(piece.currentHealth == 36)
    }

    "have applied blocking-adjustments to defender correctly" in {
      val Accept(nextGame) = attackChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 4))
      assert(piece.blockingAjustment === 0.8)
    }

    "have applied direction change to defender correctly" in {
      val Accept(nextGame) = attackChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 4))
      assert(piece.currentDirection == Vec(-1, 0)) // Facing up the board
    }

    "reject direction selection from player ID=2" in {
      val change = for {
        g1 <- attackChange
        g2 <- g1.directionSelect(PlayerId(Ids.B), -Vec.J)
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    "reject direction selection from player ID=A with bad coordinates" in {
      val change = for {
        g1 <- attackChange
        g2 <- g1.directionSelect(PlayerId(Ids.A), Vec(1, 1))
      } yield g2
      assert(change == Reject.IllegalDirSelection)
    }

    //
    // Tests below will inspect state for a valid direction change by player 1.
    //
    val directionChange = for {
      g1 <- attackChange
      g2 <- g1.directionSelect(PlayerId(Ids.A), -Vec.J)
    } yield g2


    "accept direction selection from player ID=A with dir" in {
      assert(directionChange.accepted)
    }

    "have applied direction to selected piece correctly" in {
      val Accept(nextGame) = directionChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 5))
      assert(piece.currentDirection == -Vec.J)
    }

    "have no available direction choices after changing dir" in {
      val Accept(nextGame) = directionChange
      assert(nextGame.directions.isEmpty)
    }

    "reject end turn from player ID=2 off turn" in {
      val change = for {
        g1 <- directionChange
        g2 <- g1.endTurn(PlayerId(Ids.B))
      } yield g2
      assert(change == Reject.ChangeOutOfTurn)
    }

    //
    // Tests below will inspect state for a valid end-turn action.
    //
    val endTurnChange = for {
      g1 <- directionChange
      g2 <- g1.endTurn(PlayerId(Ids.A))
      g3 <- g2.nextTurn
    } yield g3

    "have applied wait decrement on frost golem" in {
      val Accept(nextGame) = endTurnChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(8, 6))
      assert(piece.currentWait == 0)
    }

    "have applied blocking-adjustment decay" in {
      val Accept(nextGame) = endTurnChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 4))
      assert(piece.blockingAjustment === 0.72)
    }

    "have applied wait on assassin after attacking" in {
      val Accept(nextGame) = endTurnChange
      val Some(PieceLocation(p, piece)) = nextGame.board.piece(Vec(6, 5))
      assert(piece.currentWait == 1)
    }

    "correctly insert a fresh [[Turn]]" in {
      val Accept(nextGame) = endTurnChange
      assert(nextGame.turns.size == 2)
      assert(nextGame.currentTurn.actions.isEmpty)
    }
  }

}
