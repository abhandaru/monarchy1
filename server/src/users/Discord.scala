package monarchy.users

import com.fasterxml.jackson.annotation.JsonProperty

// Bindings to Discord API shape.
object Discord {
  // See also:
  // https://discord.com/developers/docs/resources/user#get-current-user
  object User {
    val Url = "https://discord.com/api/v10/users/@me"
    case class Data(
        id: String,
        username: String,
        avatar: Option[String],
        discriminator: String,
        @JsonProperty("public_flags") publicFlags: Int,
        flags: Int,
        banner: Option[String],
        @JsonProperty("accent_color") accentColor: Option[String],
        @JsonProperty("global_name") globalName: String,
        @JsonProperty("avatar_decoration_data") avatarDecorationData: Option[String],
        @JsonProperty("banner_color") bannerColor: Option[String],
        clan: Option[String],
        @JsonProperty("primary_guild") primaryGuild: Option[String],
        @JsonProperty("mfa_enabled") mfaEnabled: Boolean,
        locale: String,
        @JsonProperty("premium_type") premiumType: Int,
        email: String,
        verified: Boolean
    )
  }
}
