package monarchy.game

import scala.util.Random

case class Game(
  rand: Random,
  players: Seq[Player],
  board: Board,
  turns: Seq[Turn]
) {
  def currentTurn: Turn = turns.head

  def currentPlayer: Player = players(turns.size % players.size)

  def currentTile: Option[Tile] = {
    val selection = currentTurn.move orElse currentTurn.select
    selection.flatMap(board.tile)
  }

  def currentPiece: Option[Piece] = currentTile.flatMap(_.piece)

  def selections: Set[Vec] = {
    currentTurn.canSelect match {
      case false => Set.empty
      case true =>
        val pid = currentPlayer.id
        val tiles = board.tiles.filter(_.piece.exists(_.playerId == pid))
        tiles.map(_.point).toSet
    }
  }

  def movements: Set[Vec] = {
    val moves = for {
      tile <- currentTile
      piece <- currentPiece
      if currentTurn.canMove
    } yield {
      val points = piece.conf.movement(tile.point)
      Game.reachablePoints(board, piece, tile.point, points)
    }
    moves.getOrElse { Set.empty }
  }

  def attacks(pid: PlayerId): Set[Set[Vec]] = {
    val attackSet = for {
      tile <- currentTile
      piece <- currentPiece
      if currentTurn.canAttack
    } yield piece.conf.attackPatterns.pointSets(tile.point)
    attackSet.getOrElse { Set.empty }
  }

  def tileSelect(pid: PlayerId, p: Vec): Change[Game] = {
    playerGaurd(pid)(turnGaurd(TileSelect(p)))
  }

  def tileDeselect(pid: PlayerId): Change[Game] = {
    playerGaurd(pid)(turnGaurd(TileDeselect))
  }

  def moveSelect(pid: PlayerId, p: Vec): Change[Game] = {
    playerGaurd(pid)(turnGaurd(MoveSelect(p)))
  }

  def directionSelect(pid: PlayerId, dir: Vec): Change[Game] = {
    playerGaurd(pid)(turnGaurd(DirSelect(dir)))
  }

  def commitTurn(pid: PlayerId): Change[Game] = {
    playerGaurd(pid)(Accept(this.copy(turns = Turn() +: turns)))
  }

  def playerGaurd(pid: PlayerId)(fn: => Change[Game]): Change[Game] = {
    if (currentPlayer.id == pid) fn else Reject.ChangeOutOfTurn
  }

  def turnGaurd(act: TurnAction): Change[Game] = {
    currentTurn.act(act) match {
      case Accept(t) => Accept(this.copy(turns = t +: turns.tail))
      case r: Reject => r
    }
  }
}

object Game {
  def reachablePoints(b: Board, piece: Piece, p0: Vec, points: Deltas): Deltas = {
    def recurrence(start: Vec, visited: Deltas): Deltas = {
      val adj = Deltas.AdjecentDeltas.map(start + _)
      val valid = (adj -- visited) & points
      val accessible = valid.filter(canPassThrough(b, piece, _))
      val reached = visited ++ accessible
      reached ++ accessible.flatMap(recurrence(_, reached))
    }
    recurrence(p0, Deltas.empty).filter(canOccupy(b, _))
  }

  def canOccupy(b: Board, p: Vec): Boolean = {
    b.tile(p).exists(_.piece.isEmpty)
  }

  def canPassThrough(b: Board, piece: Piece, p: Vec): Boolean = {
    b.tile(p).exists { tile =>
      tile.piece.forall { occupant =>
        def canPass = piece.playerId == occupant.playerId && occupant.conf.movesAside
        piece.conf.teleports || canPass
      }
    }
  }
}
