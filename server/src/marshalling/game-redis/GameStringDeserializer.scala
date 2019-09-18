package monarchy.marshalling.game

import akka.util.ByteString
import redis.ByteStringDeserializer
import scala.reflect.runtime.universe.TypeTag

class GameStringDeserializer[T: TypeTag] extends ByteStringDeserializer[T] {
  override def deserialize(bs: ByteString): T = {
    GameJson.parse[T](bs.utf8String)
  }
}

object GameStringDeserializer {
  implicit def deserializer[T: TypeTag]: ByteStringDeserializer[T] = {
    new GameStringDeserializer[T]
  }
}
