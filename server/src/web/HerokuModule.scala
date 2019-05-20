package monarchy.web

object HerokuModule {
  val DefaultPort = 8080
  val EnvironmentPort = sys.env.get("PORT").map(_.toInt)
  val Port = EnvironmentPort.getOrElse(DefaultPort)
}
