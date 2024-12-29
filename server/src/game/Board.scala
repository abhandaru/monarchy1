package monarchy.game

case class Tile(
  point: Vec,
  piece: Option[Piece] = None
)

case class PieceLocation(
  point: Vec,
  piece: Piece
)

case class Board(private val tileIndex: Map[Vec, Tile]) {
  def tiles: Seq[Tile] = tileIndex.values.toSeq

  def tile(p: Vec): Option[Tile] = tileIndex.get(p)

  def pieces: Seq[PieceLocation] =
    tiles.collect(Board.PieceFilter).toSeq

  def pieces(pid: PlayerId): Seq[PieceLocation] =
    pieces.filter(_.piece.playerId == pid)

  def piece(p: Vec): Option[PieceLocation] =
    tile(p).collect(Board.PieceFilter)

  def occupied(p: Vec): Boolean =
    tile(p).exists(_.piece.nonEmpty)

  def move(from: Vec, to: Vec): Board = {
    val updated = for {
      tFrom <- tile(from)
      tTo <- tile(to)
    } yield updateTile(tFrom.copy(piece = None)).updateTile(tTo.copy(piece = tFrom.piece))
    updated.getOrElse(this)
  }

  def commit(c: TileChange): Board =
    commitAggregation(Seq(c))

  def commitAggregation(changes: Seq[TileChange]): Board = {
    changes.foldLeft(this) { case (b, change) =>
      b.tile(change.point) match {
        case None => b
        case Some(tileN) => b.updateTile(change(tileN))
      }
    }
  }

  def updateTile(t: Tile): Board = {
    tileIndex.isDefinedAt(t.point) match {
      case false => this // nothing to update
      case true => this.copy(tileIndex = tileIndex + (t.point -> t))
    }
  }

  // The bounding rectangle
  def boundingBox: (Vec, Vec) = {
    val points = tileIndex.keys.toSeq
    val is = points.map(_.i)
    val js = points.map(_.j)
    (Vec(is.min, js.min), Vec(is.max, js.max))
  }
}

object Board {
  def apply(tiles: Seq[Tile]): Board = {
    val tileIndex = tiles.map { t => t.point -> t }.toMap
    Board(tileIndex)
  }

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

  val PieceFilter: PartialFunction[Tile, PieceLocation] = {
    case t if t.piece.nonEmpty =>
      PieceLocation(t.point, t.piece.get)
  }
}

trait TileChange extends (Tile => Tile) {
  def point: Vec
}

case class PieceAdd(point: Vec, piece: Piece) extends TileChange {
  override def apply(t: Tile) = t.copy(piece = Some(piece))
}

case class PieceRemoval(point: Vec) extends TileChange {
  override def apply(t: Tile) = t.copy(piece = None)
}

case class PieceUpdate(point: Vec, fn: Piece => Piece) extends TileChange {
  override def apply(t: Tile) = t.copy(piece = t.piece.map(fn))
}
