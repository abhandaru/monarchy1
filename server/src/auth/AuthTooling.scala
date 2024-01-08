package monarchy.auth

import java.util.{Base64, UUID}
import io.jsonwebtoken.impl.crypto.MacProvider
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

object AuthTooling {
  def generateSecret: String =
    Base64.getEncoder.encodeToString(MacProvider.generateKey.getEncoded)

  def generateSignature(id: UUID, secret: String): String =
    Jwts.builder.setSubject(id.toString).signWith(SignatureAlgorithm.HS512, secret).compact
}
