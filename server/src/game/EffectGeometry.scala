package monarchy.game

object EffectGeometry {
  val BoundaryTolerance = 1e-4 // radians
  val π = math.Pi

  sealed trait Snap {
    def result: Vec
  }

  case class Clockwise90(result: Vec) extends Snap
  case class CounterClockwise90(result: Vec) extends Snap
  case class NoRotation(result: Vec) extends Snap
  case class HalfRotation(result: Vec) extends Snap

  /**
   * Given an vector `v`, determine the closest (in terms of angle) vector
   * in π/2 rotation increments over `v0`. Break ties along the diagonal to
   * either `NoRotation` or `HalfRotation`.
   */
  def directionSnap(v0: Vec, v: Vec): Snap = {
    val theta = v angle v0
    if (theta <= π/4 + BoundaryTolerance) {
      NoRotation(v0)
    } else if (theta >= 3*π/4 - BoundaryTolerance) {
      HalfRotation(-v0)
    } else if (v.cross(v0) > 0) {
      Clockwise90(-v0.perpendicular)
    } else {
      CounterClockwise90(v0.perpendicular)
    }
  }

  /**
   * For some definition of intersection, find all points "on" the line between
   * `p0` and `p`. Because we are in taxi-cab space, we will use a domain
   * specific definition here.
   *
   * Consider the "line-of-sight" between the points marked X below. The tiles
   * marked O would be in the line-of-sight.
   *
   *    X██████
   *    █O████
   *    ██O██
   *    ███X
   *    ███
   *    ██
   *    █
   *
   * WARNING: Current implementation only handles possible paths of range 6 or
   * less. Will need to add more `Trajectories` to support higher ranges.
   *
   * NOTE: A pure math solution does exist by intersecting lines with polygon
   * representations of each point on the board. Starter code:
   * https://gist.github.com/abhandaru/f743df4bdb00f47ed7b2c21e29acbd58
   */
  private val Trajectories = Map(
    Vec(1, 3) -> Seq(Vec.J, Vec(1, 2)),
    Vec(1, 4) -> Seq(Vec.J, Vec(1, 3)),
    Vec(1, 5) -> Seq(Vec.J, Vec(1, 4)),
    Vec(2, 3) -> Seq(Vec(1, 1), Vec(1, 2)),
    Vec(3, 1) -> Seq(Vec.I, Vec(2, 1)),
    Vec(3, 2) -> Seq(Vec(1, 1), Vec(2, 1)),
    Vec(4, 1) -> Seq(Vec.I, Vec(3, 1)),
    Vec(5, 1) -> Seq(Vec.I, Vec(4, 1))
  ).withDefaultValue(Nil)

  def pointsAlongSegment(p0: Vec, p: Vec): Seq[Vec] = {
    val vec = p - p0
    val vecScale = gcd(vec.i, vec.j)
    val vecReduced = vec / vecScale
    val vecLookup = Vec(math.abs(vecReduced.i), math.abs(vecReduced.j))
    // Intrinsic properties
    val theta = Vec.I angle vec
    val cross = Vec.I cross vec
    val maxScale = taxicabNorm(vec) / taxicabNorm(vecReduced)
    // Offset computation.
    val offsets = Trajectories(vecLookup) :+ vecLookup
    val offsetsCorrected = (theta <= π/2, cross > 0) match {
      case (true, true) => offsets
      case (true, false) => offsets.map { v => Vec(v.i, -v.j) }
      case (false, true) => offsets.map { v => Vec(-v.i, v.j) }
      case (false, false) => offsets.map { v => Vec(-v.i, -v.j) }
    }
    val points = for {
      v <- offsetsCorrected
      scale <- 1 to maxScale
      point = v * scale
    } yield point
    points.sortBy(_.norm).map(_ + p0)
  }

  /**
   * Utility methods to help with scale invariant transforms.
   * Consider moving into `util` package.
   */
  def taxicabNorm(v: Vec) = math.abs(v.i) + math.abs(v.j)
  def gcd(n: Int, m: Int): Int = m match {
    case 0 => n
    case mPos => gcd(mPos, n % mPos)
  }
}
