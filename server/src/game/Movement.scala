package monarchy.game

import scala.collection.mutable

object Movement {
  def reachablePoints(b: Board, loc: PieceLocation): Deltas = {
    val maxRange = loc.piece.conf.movementRange
    val queue = mutable.Queue[(Vec, Int)](loc.point -> maxRange)
    val visited = mutable.Set.empty[Vec]
    // Simple taxicab adjacency generator
    def neighbors(p: Vec): Deltas = {
      for {
        d <- Deltas.AdjecentDeltas
        pNext = p + d
        if b.tile(pNext).nonEmpty
        if canPassThrough(b, loc.piece, pNext)
      } yield pNext
    }
    // Simple BFS to find connected nodes. Note that this is differet than
    // compute reachability paths. We could revist the `visited` data structure
    // to return paths instead.
    while (queue.size > 0) {
      val (n, range) = queue.dequeue
      val ns = if (range > 0) neighbors(n) else Set.empty
      val nonCycle = ns.filterNot(visited)
      visited.add(n)
      nonCycle.foreach { ni => queue.enqueue(ni -> (range - 1)) }
    }
    visited.toSet.filterNot(b.occupied)
  }

  private def canPassThrough(b: Board, piece: Piece, p: Vec): Boolean = {
    b.tile(p).exists { tile =>
      tile.piece.forall { occupant =>
        def canPass = piece.playerId == occupant.playerId && occupant.conf.movesAside
        piece.conf.teleports || canPass
      }
    }
  }
}
