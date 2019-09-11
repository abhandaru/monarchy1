package monarchy.util

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
 * Convenience instance used across this packaged.
 * Defaults here are meant to be "sensible", for some defintion of sensible.
 */
object JsonObjectMapper extends JsonObjectMapper

class JsonObjectMapper extends ObjectMapper with ScalaObjectMapper {
  // Load serialization bindings for Java8 Instant
  registerModule(new JavaTimeModule)

  // Load serialization bindings for Scala primitives
  registerModule(DefaultScalaModule)

  // Do not fail on extra data passed in.
  configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)

  // Do not fail when serializing Scala.Unit to json
  configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

  // Write dates as milliseconds
  configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
}

