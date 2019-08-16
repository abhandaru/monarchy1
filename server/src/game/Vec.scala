package monarchy.game

object Vec {
  implicit val Ord: Ordering[Vec] = Ordering.by { v => (v.i, v.j) }
  val Zero = Vec(0, 0)
  val I = Vec(1, 0)
  val J = Vec(0, 1)
}

case class Vec(i: Int, j: Int) {
  def unary_- = Vec(-i, -j)

  def +(v: Vec): Vec = Vec(i + v.i, j + v.j)
  def -(v: Vec): Vec = Vec(i - v.i, j - v.j)
  def *(s: Int): Vec = Vec(i * s, j * s)
  def /(s: Int): Vec = Vec(i / s, j / s)

  def dot(v: Vec): Int = i * v.i + j * v.j
  def norm: Double = math.sqrt(i * i + j * j)

  /** Returned angle is always between 0 and π */
  def angle(v: Vec): Double = math.acos(dot(v) / (norm * v.norm))

  /**
   * The cross product in 2-space boils down to the following.
   *
   *  i   j  k
   *  i1 j1 k1
   *  i2 j2 k2
   *
   * For vectors in 2-space, the K component is always 0, so something fairly
   * simple falls out of the math.
   *
   *  i * (j1*k2 - k1*j2) + j * (i1*k2 - k1*i2) + k * (i1*j2 - j1*i2)
   *  0 + 0 + k * (i1*j2 - j1*i2)
   *  i1*j2 - j1*i2
   */
  def cross(v: Vec): Int = i * v.j - j * v.i

  /**
   * The 2-space rotation boils down to something very simple.
   * See: https://en.wikipedia.org/wiki/Rotation_matrix
   */
  def perpendicular: Vec = Vec(-j, i)
}
