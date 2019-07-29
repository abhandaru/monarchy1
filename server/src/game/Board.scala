package monarchy.game

case class Tile(
  point: Vec,
  piece: Option[Piece] = None
)

case class Board(tiles: Seq[Tile]) {
  def placePiece(p: Vec, piece: Piece): Board = {
    this.copy(tiles = tiles.map { t =>
      if (t.point == p) t.copy(piece = Some(piece)) else t
    })
  }
}

object Board {
  val Standard: Board = {
    val size = 11
    val taperSize = 3
    val points = for {
      row <- 0 until size
      col <- 0 until size
    } yield Vec(row, col)
    val legalPoints = points.filterNot { case Vec(r, c) =>
      val tl = (r + c) < taperSize;
      val bl = ((size - 1 - r) + c) < taperSize;
      val tr = (r + (size - 1 - c)) < taperSize;
      val br = ((size - 1 - c) + (size - 1 - r)) < taperSize;
      tl || bl || tr || br
    }
    Board(legalPoints.map(Tile(_, None)))
  }
}
