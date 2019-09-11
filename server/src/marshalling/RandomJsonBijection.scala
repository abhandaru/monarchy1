package monarchy.marshalling

import java.io
import java.util.Base64
import scala.util.Random

object RandomJsonBijection extends JsonBijection[Random] {
  override def apply(rand: Random): String = {
    val baos = new io.ByteArrayOutputStream
    val stream = new io.ObjectOutputStream(baos)
    stream.writeObject(rand)
    val bytes = baos.toByteArray
    stream.close
    Base64.getUrlEncoder.encodeToString(bytes)
  }

  override def invert(json: String): Random = {
    val bytes = Base64.getUrlDecoder.decode(json)
    val stream = new io.ObjectInputStream(new io.ByteArrayInputStream(bytes))
    val rand = stream.readObject.asInstanceOf[Random]
    stream.close
    rand
  }
}
