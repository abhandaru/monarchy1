package monarchy.game

case class Tile(
  point: Vec,
  piece: Option[Piece] = None
)

case class PlacedPiece(
  point: Vec,
  piece: Piece
)

case class Board(tiles: Seq[Tile]) {
  def tile(p: Vec): Option[Tile] =
    tiles.find(_.point == p)

  def pieces: Seq[PlacedPiece] =
    tiles.collect { case t if t.piece.nonEmpty => PlacedPiece(t.point, t.piece.get) }

  def pieces(pid: PlayerId): Seq[PlacedPiece] =
    pieces.filter(_.piece.playerId == pid)

  def place(piece: PlacedPiece): Board =
    updateTile(piece.point, Some(piece.piece))

  def remove(point: Vec): Board =
    updateTile(point, None)

  def move(from: Vec, to: Vec): Board = {
    val piece = tile(from).flatMap(_.piece)
    updateTile(from, None).updateTile(to, piece)
  }

  def updateTile(p: Vec, piece: Option[Piece]): Board = {
    this.copy(tiles = tiles.map { t =>
      if (t.point == p) t.copy(piece = piece) else t
    })
  }
}

object Board {
  val Standard: Board = {
    val size = 11
    val taperSize = 2
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
