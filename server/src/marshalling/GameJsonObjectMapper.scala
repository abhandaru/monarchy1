package monarchy.marshalling

import monarchy.game._
import monarchy.util.JsonObjectMapper
import com.fasterxml.jackson.core.{JsonParser, JsonGenerator}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, DeserializationFeature, ObjectMapper, SerializerProvider}
import scala.reflect.ClassTag

object GameJsonObjectMapper extends JsonObjectMapper {
  registerModule(GameModule)
}

object GameModule extends SimpleModule {
  addSerializer(RandomTo.sym, RandomTo)
  addDeserializer(RandomFrom.sym, RandomFrom)
}

abstract class BijectionDeserializer[T: ClassTag](bjt: JsonBijection[T])
  extends StdDeserializer[Any](implicitly[ClassTag[T]].runtimeClass) {
  val sym: Class[Any] = _valueClass.asInstanceOf[Class[Any]]
  override def deserialize(jp: JsonParser, ctx: DeserializationContext): T = {
    bjt.invert(jp.getText)
  }
}

abstract class BijectionSerializer[T: ClassTag](bjt: JsonBijection[T])
  extends StdSerializer[Any](implicitly[ClassTag[T]].runtimeClass, true) {
  val sym: Class[Any] = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[Any]]
  override def serialize(value: Any, jgen: JsonGenerator, pvd: SerializerProvider) = {
    jgen.writeString(bjt.apply(value.asInstanceOf[T]))
  }
}

  object RandomTo extends BijectionSerializer[scala.util.Random](RandomJsonBijection)
  object RandomFrom extends BijectionDeserializer[scala.util.Random](RandomJsonBijection)
