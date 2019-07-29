package monarchy.game

case class Vec(i: Int, j: Int) {
  def +(v: Vec): Vec = Vec(i + v.i, j + v.j)
  def -(v: Vec): Vec = Vec(i - v.i, j - v.j)
  def *(s: Int): Vec = Vec(i * s, j * s)
}
