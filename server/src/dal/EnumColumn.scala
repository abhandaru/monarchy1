package monarchy.dal

import scala.language.implicitConversions
import scala.reflect.{ClassTag, runtime}
import scala.util.Try

trait EnumColumn {
  def id: Int
  def name: String = this.toString
}

class EnumColumnDef[E <: EnumColumn: ClassTag] { self =>
  import PostgresProfile.Implicits._

  def apply(id: Int): E =
    values.find(_.id == id).get

  def withName(name: String): E =
    values.find(_.name == name).get

  lazy val values: Iterable[E] = {
    import runtime.universe._
    val mirror = runtimeMirror(self.getClass.getClassLoader)
    val classSymbol = mirror.classSymbol(self.getClass)
    classSymbol.toType.members
      .filter(_.isModule)
      .map { symbol => mirror.reflectModule(symbol.asModule).instance }
      .collect { case v: E => v }
  }

  implicit val enumColumnType: BaseColumnType[E] =
    MappedColumnType.base[E, Int](_.id, apply)

  implicit def enumColumnRep(e: E): Rep[E] =
    LiteralColumn(e)
}
