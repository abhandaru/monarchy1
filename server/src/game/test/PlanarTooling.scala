package monarchy.game

object PlanarTooling {
  val EmptyChars = Set('.', '░')
  val SpaceChars = Set('#', '█')

  case class Substitution(vec: Vec, index: Option[Int])
  case class Plane(rep: String, origin: Vec = Deltas.Origin)

  def substitutions(plane: Plane): Seq[Substitution] = {
    val Plane(rep, origin) = plane
    val lines = rep.trim.stripMargin.split("\n")
    for {
      (line, i) <- lines.zipWithIndex
      (char, j) <- line.zipWithIndex
      if !SpaceChars(char)
      index = Option(char).filterNot(EmptyChars).map(_ - 48)
    } yield Substitution(Vec(i, j) - origin, index)
  }

  def points(plane: Plane): Set[Vec] = {
    substitutions(plane).map(_.vec).toSet
  }

  def compare[T: PlanarToolingSupport](ts: Iterable[T], plane: Plane): Boolean = {
    val conversion = implicitly[PlanarToolingSupport[T]]
    val ref = points(plane)
    val test = ts.map(conversion).toSet
    if (ref == test) true else {
      println(s"[PlanarTooling] missing - ${(ref -- test).mkString(", ")}")
      println(s"[PlanarTooling] extra   + ${(test -- ref).mkString(", ")}")
      println(s"[PlanarTooling] candidate:\n${render(ts)}")
      false
    }
  }

  def compare[T: PlanarToolingSupport](ts: Iterable[T], rep: String): Boolean =
    compare(ts, rep.plane)

  // Useful for debugging and printing vector sets received by the caller
  // method. When `compare` fails it will automatically dump the test value.
  def render[T](ts: Iterable[T])(implicit pts: PlanarToolingSupport[T]): String = {
    if (ts.isEmpty) return ""
    val vectors = ts.map(pts).toSet
    val iMax = vectors.map(_.i).max
    val jMax = vectors.map(_.j).max
    val chars2D = for (i <- 0 to iMax) yield {
      for (j <- 0 to jMax) yield {
        if (vectors(Vec(i, j))) '░' else '█'
      }
    }
    chars2D.map(_.mkString).mkString("\n")
  }

  implicit class PlanarStringOps(val rep: String) extends AnyVal {
    def plane: Plane = Plane(rep)
    def origin(i: Int, j: Int): Plane = Plane(rep, Vec(i, j))
  }
}

trait PlanarToolingSupport[T] extends (T => Vec)

object PlanarToolingSupport {
  class Support[T](ext: T => Vec) extends PlanarToolingSupport[T] {
    override def apply(t: T) = ext(t)
  }

  // implicit def tileChange[T <: TileChange]: PlanarToolingSupport[T] = new Support(_.point)
  implicit object PieceLocation extends Support[PieceLocation](_.point)
  implicit object EffectLocation extends Support[EffectLocation](_.point)
  implicit object Effect extends Support[Effect]({
    case HealAll(_) => Deltas.Origin
    case e: LocalEffect => e.point
  })
  implicit object Tile extends Support[Tile](_.point)
  implicit object Vec extends Support[Vec](identity)
}
