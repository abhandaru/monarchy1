package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer

object WebServer extends App {
  implicit val system = ActorSystem("monarchy-web")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val queryCli = DatabaseModule.queryClient
  val port = HerokuModule.Port
  val route = logRequestResult("monarchy-web") {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  }
  val routeLogged = DebuggingDirectives.logRequestResult("LOG:", Logging.InfoLevel)(route)
  val routeBindings = Http().bindAndHandle(routeLogged, HerokuModule.Host, port)

  println(s"web-server online at port=$port")
}
