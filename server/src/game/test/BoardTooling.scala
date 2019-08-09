package monarchy.game

object BoardTooling {
  def from(rep: String, pieces: Seq[Piece] = Nil): Board = {
    val substitutions = PlanarTooling.substitutions(PlanarTooling.Plane(rep))
    val tiles = substitutions.map { sub =>
      Tile(sub.vec, sub.index.flatMap(pieces.lift))
    }
    Board(tiles)
  }
}
