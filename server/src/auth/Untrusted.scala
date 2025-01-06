package monarchy.auth

import java.util.{Base64, UUID}
import monarchy.util.Json
import scala.util.Try

object Untrusted {
  def extractUserId(bearer: String): Try[UUID] =
    extractSubject(bearer).map(UUID.fromString)

  def extractSubject(bearer: String): Try[String] =
    Try(extractSubjectUnsafe(bearer))

  private case class Claims(sub: String)

  private def extractSubjectUnsafe(bearer: String): String = {
    val jwt = bearer.stripPrefix("Bearer ")
    val claimsBase64 = jwt.split("\\.")(1)
    val claimsJson = new String(Base64.getDecoder.decode(claimsBase64))
    val claims = Json.parse[Claims](claimsJson)
    claims.sub
  }
}
