package monarchy

import akka.util.ByteString
import monarchy.util.Json
import redis.ByteStringDeserializer
import scala.reflect.runtime.universe.TypeTag

package object streaming {
  // When reading keys from Redis with an explicit type, assume JSON.
  implicit def redisValueMarshaller[T: TypeTag]: ByteStringDeserializer[T] = {
    new ByteStringDeserializer[T] {
      override def deserialize(bs: ByteString): T = {
        Json.parse[T](bs.utf8String)
      }
    }
  }
}
