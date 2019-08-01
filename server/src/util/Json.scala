package monarchy.util

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.StringWriter
import java.lang.reflect.{ParameterizedType => JParameterizedType, Type => JType}
import scala.reflect.runtime.universe._
import scala.util.Try

/**
 * Get type reference to battle Java eraser, see:
 * https://github.com/FasterXML/jackson-module-scala/issues/105
 */
class JsonTypeReference[T: TypeTag] extends TypeReference[T] {
  import JsonTypeReference._
  override def getType = extractType(typeOf[T])
}

object JsonTypeReference {
  val Mirror = runtimeMirror(this.getClass.getClassLoader)
  def extractType(paramType: Type): JType = {
    val ctor = Mirror.runtimeClass(paramType)
    val innerTypes = paramType.typeArgs.map(extractType).toList
    innerTypes match {
      case Nil => ctor
      case inners =>
        new JParameterizedType {
          override def getRawType: JType = ctor
          override def getActualTypeArguments: Array[JType] = inners.toArray
          override def getOwnerType: JType = null
        }
    }
  }
}

object Json {
  def parse[T: TypeTag](json: String): T = parseWith(JsonObjectMapper, json)

  def parseAttempt[T: TypeTag](json: String): Try[T] = Try(parse[T](json))

  // See this post for origins:
  // https://stackoverflow.com/a/29342108/408940
  def parseWith[T: TypeTag](m: ObjectMapper, json: String): T = {
    m.readValue(json, new JsonTypeReference[T]).asInstanceOf[T]
  }

  def stringify[T: TypeTag](ref: T): String = stringifyWith(JsonObjectMapper, ref)

  def stringifyWith[T: TypeTag](m: ObjectMapper, ref: T): String = {
    val wr = new StringWriter
    m.writeValue(wr, ref)
    wr.toString
  }
}
