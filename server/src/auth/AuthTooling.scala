package monarchy.auth

import java.util.Base64
import io.jsonwebtoken.impl.crypto.MacProvider
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

object AuthTooling {
  def generateSecret: String = {
    Base64.getEncoder().encodeToString(MacProvider.generateKey.getEncoded)
  }

  def generateSignature(userId: Long, secret: String): String = {
    Jwts.builder.setSubject(userId.toString).signWith(SignatureAlgorithm.HS512, secret).compact
  }
}
