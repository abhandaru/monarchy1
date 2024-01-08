package monarchy.util

object Format {
  // Basically convert to E164 format
  def normalizePhoneNumber(raw: String): String =
    "+1" + raw.stripPrefix("+1")
}
