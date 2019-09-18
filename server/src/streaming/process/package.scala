package monarchy.streaming

import monarchy.marshalling.game.GameStringDeserializer
import scala.reflect.runtime.universe.TypeTag

package object process {
  // When reading keys from Redis with an explicit type, assume JSON.
  implicit def redisValueMarshaller[T: TypeTag] = GameStringDeserializer.deserializer
}
