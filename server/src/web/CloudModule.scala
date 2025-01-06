package monarchy.web

object CloudModule {
  val DefaultPort = 8080
  val EnvironmentPort = sys.env.get("PORT").map(_.toInt)
  val Port = EnvironmentPort.getOrElse(DefaultPort)
  val Host = "0.0.0.0"
}
