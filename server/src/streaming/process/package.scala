package monarchy.streaming

import monarchy.marshalling.game.GameStringDeserializer
import monarchy.streaming.core.StreamAction
import scala.concurrent.Future
import scala.reflect.runtime.universe.TypeTag

package object process {
  // Convenient return type.
  val AsyncNullAction = Future.successful(StreamAction.Null)

  // When reading keys from Redis with an explicit type, assume JSON.
  implicit def redisValueMarshaller[T: TypeTag] = GameStringDeserializer.deserializer
}
