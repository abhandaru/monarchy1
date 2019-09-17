package monarchy.game

/**
 * A 2x2 matrix, useful for translation and rotations.
 * See also: https://en.wikipedia.org/wiki/Rotation_matrix
 *
 *   +------+
 *   | a  b |
 *   | c  d |
 *   +------+
 *
 */
case class Matrix(a: Int, b: Int, c: Int, d: Int) {
  def *(v: Vec): Vec = Vec(a * v.i + b * v.j, c * v.i + d * v.j)
  def *(m: Matrix): Matrix = Matrix(
    (a * m.a + b * m.c), (a * m.b + b * m.d),
    (c * m.a + d * m.c), (c * m.b + d * m.d)
  )

  def ^(i: Int): Matrix = i match {
    case n if n > 1 => List.fill(n - 1)(this).reduce(_ * _)
    case 1 => this
    case _ => Matrix.Identity
  }
}

object Matrix {
  val Identity = Matrix(1, 0, 0, 1)
  val Rotate90CC = Matrix(0, -1, 1, 0)
  val Rotate180 = Matrix(-1, 0, 0, -1)
}
