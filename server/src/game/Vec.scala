package monarchy.game

object Vec {
  implicit val Ord: Ordering[Vec] = Ordering.by { v => (v.i, v.j) }
}

case class Vec(i: Int, j: Int) {
  def +(v: Vec): Vec = Vec(i + v.i, j + v.j)
  def -(v: Vec): Vec = Vec(i - v.i, j - v.j)
  def *(s: Int): Vec = Vec(i * s, j * s)

  def dot(v: Vec): Int = i * v.i + j * v.j
  def norm: Double = math.sqrt(i * i + j * j)

  /** Returned angle is always between 0 and π */
  def angle(v: Vec): Double = math.acos(dot(v) / (norm * v.norm))

  /**
   * The curl is the 3rd component of the cross-product.
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
  def curl(v: Vec): Int = i * v.j - j * v.i

  def perpendicular: Vec = Vec(-j, -i)
}
