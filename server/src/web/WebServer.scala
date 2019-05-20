package monarchy.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object WebServer extends App {
  implicit val system = ActorSystem("web-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val queryCli = DatabaseModule.queryClient
  val port = HerokuModule.Port
  val route = {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  }
  val routeBindings = Http().bindAndHandle(route, "localhost", port)

  println(s"Server online at $port")
}
