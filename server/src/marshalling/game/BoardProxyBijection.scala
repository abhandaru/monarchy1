package monarchy.marshalling.game

import monarchy.game.{Board, Tile}
import monarchy.util.Bijection

case class BoardProxy(tiles: Seq[Tile])

object BoardProxyBijection extends Bijection[Board, BoardProxy] {
  override def apply(board: Board) = BoardProxy(board.tiles)
  override def invert(proxy: BoardProxy) = Board(proxy.tiles)
}
