package monarchy.graphql

import sangria.schema.{EnumType, EnumValue}

object EnumUtil {
  def mkEnum[A](name: String, values: Set[A]): EnumType[A] = {
    val valuesList = values.map(mkValue).toList
    new EnumType[A](name = name, values = valuesList) {
      override def coerceOutput(value: A): String =
        byValue.get(value).map(_.name).getOrElse { "_ERROR" }
    }
  }

  private def mkValue[A](v: A): EnumValue[A] =
    EnumValue[A](name = format(v), value = v)

  private def format[A](v: A): String =
    v.toString.replaceAll("([a-z]|[0-9])([A-Z])", "$1_$2").toUpperCase
}
