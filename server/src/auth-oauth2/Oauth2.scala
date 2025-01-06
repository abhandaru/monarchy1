package monarchy.auth.oauth2

import akka.http.scaladsl.model.Uri
import com.fasterxml.jackson.annotation.JsonProperty

object Oauth2 {
  case class Config(
      baseUrl: String,
      clientId: String,
      clientSecret: String,
      redirectUri: String,
      scopes: Set[String] = Set.empty
  )

  object Token {
    case class Request(
        @JsonProperty("grant_type") grantType: String = "authorization_code",
        code: Option[String] = None,
        @JsonProperty("redirect_uri") redirectUri: Option[String] = None,
        scope: Option[String] = None,
    )

    case class Response(
        @JsonProperty("token_type") tokenType: String,
        @JsonProperty("access_token") accessToken: String,
        @JsonProperty("expires_in") expiresIn: Int,
    )
  }

  object Callback {
    sealed trait Data
    case object ErrorOther extends Data
    case class Error(error: String, desc: String) extends Data
    case class Success(code: String, state: String) extends Data

    def parse(query: Uri.Query): Data = {
      val error = for {
        error <- query.get("error")
        desc <- query.get("error_description")
      } yield Error(error, desc)
      val success = for { 
        code <- query.get("code")
        state <- query.get("state")
      } yield Success(code, state)
      error.orElse(success).getOrElse(ErrorOther)
    }
  }
}
