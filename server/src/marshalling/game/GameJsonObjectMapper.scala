package monarchy.marshalling.game

import monarchy.game._
import monarchy.util.JsonObjectMapper
import com.fasterxml.jackson.core.{JsonParser, JsonGenerator}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, DeserializationFeature, ObjectMapper, SerializerProvider}
import scala.reflect.ClassTag
import scala.util.Random

object GameJsonObjectMapper extends JsonObjectMapper {
  registerModule(GameModule)
}

object GameModule extends SimpleModule {
  addDeserializer(BoardFrom.sym, BoardFrom)
  addDeserializer(PieceConfFrom.sym, PieceConfFrom)
  addDeserializer(RandomFrom.sym, RandomFrom)
  addDeserializer(TurnActionFrom.sym, TurnActionFrom)
  addDeserializer(EffectFrom.sym, EffectFrom)
  addSerializer(classOf[Board], BoardTo)
  addSerializer(classOf[PieceConf], PieceConfTo)
  addSerializer(classOf[Random], RandomTo)
  addSerializer(classOf[TurnAction], TurnActionTo)
  addSerializer(classOf[Effect], EffectTo)
}

object TypeUtil {
  def classFromTag[T: ClassTag]: Class[_] = {
    implicitly[ClassTag[T]].runtimeClass
  }
}
import TypeUtil._

abstract class Serializer[T: ClassTag] extends StdSerializer[Any](classFromTag[T], true)
abstract class Deserializer[T: ClassTag] extends StdDeserializer[Any](classFromTag[T]) {
  val sym: Class[Any] = _valueClass.asInstanceOf[Class[Any]]
}

class ProxySerializer[T: ClassTag](map: T => Any) extends Serializer[T] {
  override def serialize(value: Any, jgen: JsonGenerator, pvd: SerializerProvider) = {
    pvd.defaultSerializeValue(map(value.asInstanceOf[T]), jgen)
  }
}

class ProxyDeserializer[A: ClassTag, B: ClassTag](map: B => A) extends Deserializer[A] {
  override def deserialize(jp: JsonParser, ctx: DeserializationContext): A = {
    map(ctx.readValue(jp, classFromTag[B]).asInstanceOf[B])
  }
}

object RandomTo extends ProxySerializer(RandomStringBijection)
object RandomFrom extends ProxyDeserializer(RandomStringBijection.inverse)
object PieceConfTo extends ProxySerializer(PieceConfStringBijection)
object PieceConfFrom extends ProxyDeserializer(PieceConfStringBijection.inverse)
object BoardTo extends ProxySerializer(BoardProxyBijection)
object BoardFrom extends ProxyDeserializer(BoardProxyBijection.inverse)
object TurnActionTo extends ProxySerializer(TurnActionProxyBijection)
object TurnActionFrom extends ProxyDeserializer(TurnActionProxyBijection.inverse)
object EffectTo extends ProxySerializer(EffectProxyBijection)
object EffectFrom extends ProxyDeserializer(EffectProxyBijection.inverse)
