package monarchy.streaming

import akka.util.ByteString
import monarchy.marshalling.GameJson
import redis.ByteStringDeserializer
import scala.reflect.runtime.universe.TypeTag

package object process {
  // When reading keys from Redis with an explicit type, assume JSON.
  implicit def redisValueMarshaller[T: TypeTag]: ByteStringDeserializer[T] = {
    new ByteStringDeserializer[T] {
      override def deserialize(bs: ByteString): T = {
        GameJson.parse[T](bs.utf8String)
      }
    }
  }
}
