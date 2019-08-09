package monarchy.game

object PlanarTooling {
  val EmptyChars = Set('.', '░')
  val SpaceChars = Set('#', '█')

  case class Substitution(vec: Vec, index: Option[Int])

  def substitutions(rep: String): Seq[Substitution] = {
    val lines = rep.trim.stripMargin.split("\n")
    for {
      (line, i) <- lines.zipWithIndex
      (char, j) <- line.zipWithIndex
      if !SpaceChars(char)
      index = Option(char).filterNot(EmptyChars).map(_ - 48)
    } yield Substitution(Vec(i, j), index)
  }

  def points(rep: String): Set[Vec] = {
    substitutions(rep).map(_.vec).toSet
  }

  def compare[T: PlanarToolingSupport](rep: String, ts: Iterable[T]): Boolean = {
    val conversion = implicitly[PlanarToolingSupport[T]]
    val ref = points(rep)
    val test = ts.map(conversion).toSet
    if (ref == test) true else {
      println(s"PlanarTooling - want ${ref.size} points")
      println(s"PlanarTooling - found ${test.size} points")
      false
    }
  }
}

trait PlanarToolingSupport[T] extends (T => Vec)

object PlanarToolingSupport {
  class Support[T](ext: T => Vec) extends PlanarToolingSupport[T] {
    override def apply(t: T) = ext(t)
  }

  implicit object VecSupport extends Support[Vec](identity)
  implicit object TileSupport extends Support[Tile](_.point)
}
