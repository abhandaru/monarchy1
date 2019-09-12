package monarchy.marshalling

import monarchy.game.{Board, Tile}
import monarchy.util.Json

case class BoardProxy(tiles: Seq[Tile])

object BoardProxyBijection extends Bijection[Board, BoardProxy] {
  override def apply(board: Board) = BoardProxy(board.tiles)
  override def invert(proxy: BoardProxy) = Board(proxy.tiles)
}
