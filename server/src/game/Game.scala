package monarchy.game

import scala.util.Random

case class Game(
  rand: Random,
  players: Seq[Player],
  board: Board,
  turns: Seq[Turn]
) {
  def currentTurn: Turn = turns.head

  def currentPlayer: Player =
    players((turns.size - 1) % players.size)

  def currentSelection: Option[Vec] =
    currentTurn.move orElse currentTurn.select

  def currentTile: Option[Tile] =
    currentSelection.flatMap(board.tile)

  def currentPiece: Option[Piece] =
    currentTile.flatMap(_.piece)

  def selections: Deltas = {
    currentTurn.canSelect match {
      case false => Set.empty
      case true =>
        val pid = currentPlayer.id
        val tiles = board.tiles.filter(_.piece.exists(_.playerId == pid))
        tiles.map(_.point).toSet
    }
  }

  def movements: Deltas = {
    val moveSet = for {
      tile <- currentTile
      piece <- currentPiece
      if currentTurn.canMove
      if piece.canAct
    } yield {
      val points = piece.conf.movement(tile.point)
      Game.reachablePoints(board, piece, tile.point, points)
    }
    moveSet.getOrElse { Deltas.empty }
  }

  def directions: Deltas = {
    val dirSet = for {
      piece <- currentPiece
      if currentTurn.canDir
      if piece.canAct
    } yield Deltas.AdjecentDeltas
    dirSet.getOrElse { Set.empty }
  }

  def attacks: Set[Deltas] = {
    val attackSet = for {
      tile <- currentTile
      piece <- currentPiece
      if currentTurn.canAttack
      if piece.canAct
    } yield piece.conf.attackPatterns.pointSets(tile.point)
    attackSet.getOrElse { Set.empty }
  }

  def effects(attack: Deltas): Set[EffectLocation] = {
    val effectSet = for {
      tile <- currentTile
      piece <- currentPiece
    } yield {
      val pattern = PointPattern.infer(tile.point, attack)
      val rawEffects = piece.conf.effectArea(tile.point, pattern)
      rawEffects.flatMap(EffectLocation(board, PieceLocation(tile.point, piece), _))
    }
    effectSet.getOrElse { Set.empty }
  }

  def tileSelect(pid: PlayerId, p: Vec): Change[Game] =
    playerGuard(pid).flatMap(_ => turnGuard(TileSelect(p)))

  def tileDeselect(pid: PlayerId): Change[Game] =
    playerGuard(pid).flatMap(_ => turnGuard(TileDeselect))

  def moveSelect(pid: PlayerId, p: Vec): Change[Game] = {
    for {
      _ <- playerGuard(pid)
      game <- turnGuard(MoveSelect(p))
      nextGame <- currentTile match {
        case None => Reject.PieceActionWithoutSelection
        case Some(tile) =>
          tile.piece match {
            case None => Reject.PieceActionWithoutSelection
            case Some(_) if !movements(p) => Reject.IllegalMoveSelection
            case Some(piece) if piece.playerId != pid => Reject.PieceActionWithoutOwnership
            case Some(piece) =>
              Accept(game.copy(board = game.board.move(tile.point, p)))
          }
      }
    } yield nextGame
  }

  def directionSelect(pid: PlayerId, dir: Vec): Change[Game] = {
    for {
      _ <- playerGuard(pid)
      game <- turnGuard(DirSelect(dir))
      nextGame <- currentTile match {
        case None => Reject.PieceActionWithoutSelection
        case Some(tile) =>
          tile.piece match {
            case None => Reject.PieceActionWithoutSelection
            case Some(_) if !directions(dir) => Reject.IllegalDirSelection
            case Some(piece) if piece.playerId != pid => Reject.PieceActionWithoutOwnership
            case Some(piece) =>
              val nextPiece = PieceLocation(tile.point, piece.copy(currentDirection = dir))
              val nextBoard = game.board.commit(nextPiece)
              Accept(game.copy(board = nextBoard))
          }
      }
    } yield nextGame
  }

  def attackSelect(pid: PlayerId, attack: Deltas): Change[Game] = {
    import EffectGeometry._
    for {
      _ <- playerGuard(pid)
      game <- turnGuard(AttackSelect(attack))
      nextGame <- currentTile match {
        case None => Reject.PieceActionWithoutSelection
        case Some(tile) =>
          tile.piece match {
            case None => Reject.PieceActionWithoutSelection
            case Some(_) if !attacks(attack) => Reject.IllegalAttackSelection
            case Some(piece) if piece.playerId != pid => Reject.PieceActionWithoutOwnership
            case Some(piece) =>
              val pattern = PointPattern.infer(tile.point, attack)
              val effects = piece.conf.effectArea(tile.point, pattern)
              val effectLocations = effects.flatMap(
                EffectLocation(game.board, PieceLocation(tile.point, piece), _)
              ).toSeq.sorted
              val effectUpdates = effectLocations.flatMap {
                // Damage another unit. Compute blocking, directionality, and damage.
                case EffectLocation(pt, Attack(_, power)) =>
                  game.board.tile(pt) match {
                    case None => None
                    case Some(Tile(_, None)) => None
                    case Some(Tile(_, Some(pieceN))) => Some {
                      def damage = math.round(power * (1 - pieceN.conf.armor)).toInt
                      def canBlock = piece.conf.blockable && pieceN.canBlock
                      if (canBlock) {
                        val blockingBase = pieceN.conf.blocking
                        val blocking = pieceN.currentBlocking
                        // Compute angle of vec from origin to target with way unit is facing.
                        val attackDir = pt - tile.point
                        val snapped = directionSnap(piece.currentDirection, attackDir)
                        val (anchor, blockingProb, blockingDir) = snapped match {
                          case Clockwise90(dir) =>        (2, blocking / 2, -dir)
                          case CounterClockwise90(dir) => (2, blocking / 2, -dir)
                          case HalfRotation(dir) =>       (1,     blocking, -dir)
                          case NoRotation(dir) =>         (0,          0.0, -dir)
                        }
                        val blockingOutcome = rand.nextDouble <= blockingProb
                        if (blockingOutcome) {
                          PieceLocation(pt, pieceN.copy(
                            currentDirection = blockingDir,
                            blockingAjustment = pieceN.blockingAjustment - (anchor - blockingBase)
                          ))
                        } else {
                          val health = math.max(pieceN.currentHealth - damage, 0)
                          health match {
                            case 0 => PieceRemoval(pt)
                            case h => PieceLocation(pt, pieceN.copy(
                              blockingAjustment = pieceN.blockingAjustment + blockingBase,
                              currentHealth = h
                            ))
                          }
                        }
                      } else {
                        val health = math.max(pieceN.currentHealth - damage, 0)
                        health match {
                          case 0 => PieceRemoval(pt)
                          case h => PieceLocation(pt, pieceN.copy(currentHealth = h))
                        }
                      }
                    }
                  }
                // Adds a shrub unit for unoccupied tiles.
                case EffectLocation(pt, GrowPlant(_)) =>
                  if (game.board.occupied(pt))
                    None
                  else
                    Some(PieceLocation(pt, PieceBuilder(Shrub, pid, piece.currentDirection)))
                // Heals all units for the same player
                case EffectLocation(pt, HealAll(power)) =>
                  game.board.piece(pt).map {
                    case pl @ PieceLocation(_, pieceN) =>
                      pl.copy(piece = pieceN.copy(currentHealth = pieceN.currentHealth + power))
                  }
                // Paralyzes the targeted tiles
                case EffectLocation(pt, Paralyze(_)) =>
                  game.board.piece(pt).map {
                    case pl @ PieceLocation(_, pieceN) =>
                      pl.copy(piece = pieceN.copy(paralyzed = true))
                  }
              }
              // The attacker turns in the snapped avg. direction of the attacks.
              val attackerUpdate = {
                val attackDir = attack.foldLeft(Deltas.Origin)(_ + _ - tile.point)
                val nextDir = directionSnap(piece.currentDirection, attackDir).result
                PieceLocation(tile.point, piece.copy(currentDirection = nextDir))
              }
              // Fold updates over the board-state.
              val updates = effectUpdates :+ attackerUpdate
              val nextBoard = game.board.commit(updates)
              val nextGame = game.copy(board = nextBoard)
              Accept(nextGame)
          }
      }
    } yield nextGame
  }

  def commitTurn(pid: PlayerId): Change[Game] = {
    playerGuard(pid).map { _ =>
      val updatesForAll = board.pieces.map {
        case pl @ PieceLocation(pt, piece) =>
          val waitDecrement = if (pid == piece.playerId) 1 else 0
          pl.copy(
            piece = piece.copy(
              currentWait = piece.currentWait - waitDecrement,
              blockingAjustment = piece.blockingAjustment * Game.BlockingAdjustmentDecay
            )
          )
      }
      val updateForPiece = for {
        tile <- currentTile
        piece <- tile.piece
      } yield {
        val totalWait = currentTurn.actions.collect {
          case MoveSelect(_) => math.floor(piece.conf.maxWait / 2.0).toInt
          case AttackSelect(_) => math.ceil(piece.conf.maxWait / 2.0).toInt
        }.sum
        PieceLocation(tile.point, piece.copy(currentWait = totalWait))
      }
      val updates = updatesForAll ++ updateForPiece.toSeq
      val nextBoard = board.commit(updates)
      this.copy(board = nextBoard, turns = Turn() +: turns)
    }
  }

  def playerGuard(pid: PlayerId): Change[Unit] = {
    if (currentPlayer.id == pid) Accept.Unit else Reject.ChangeOutOfTurn
  }

  def turnGuard(act: TurnAction): Change[Game] = {
    currentTurn.act(act).map(t => this.copy(turns = t +: turns.tail))
  }
}

object Game {
  val BlockingAdjustmentDecay = 0.9

  def reachablePoints(b: Board, piece: Piece, p0: Vec, points: Deltas): Deltas = {
    def neighbors(p: Vec): Deltas = {
      for {
        d <- Deltas.AdjecentDeltas
        pNext = p + d
        if points(pNext)
        if canPassThrough(b, piece, pNext)
      } yield pNext
    }
    GraphOperations.reachableFrom(p0, neighbors)
      .filterNot(b.occupied)
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
