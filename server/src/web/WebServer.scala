package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import monarchy.controllers._

object WebServer extends App {
  implicit val system = ActorSystem("monarchy-web")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val queryCli = DatabaseModule.queryClient
  val rootController = new RootController

  val port = HerokuModule.Port
  val route = logRequestResult("monarchy-web") {
    pathSingleSlash(rootController)
  }
  val routeLogged = DebuggingDirectives.logRequestResult("LOG:", Logging.InfoLevel)(route)
  val routeBindings = Http().bindAndHandle(routeLogged, HerokuModule.Host, port)

  println(s"web-server online at port=$port")
}
