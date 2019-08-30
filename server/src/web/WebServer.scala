package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import monarchy.auth.AuthFilter
import monarchy.controllers._
import monarchy.streaming.StreamingFlow

object WebServer extends App {
  implicit val system = ActorSystem("monarchy-web")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val queryCli = DatabaseModule.queryClient
  implicit val redisCli = RedisModule.redisClient
  implicit val graphqlContext = GraphqlModule.graphqlContext

  // Request handlers
  implicit val webSocketService = StreamingFlow(RedisModule.redisActorPublisher)

  // TODO (adu): Remove, just for testing!
  import scala.concurrent.duration._
  system.scheduler.schedule(2 seconds, 2 seconds)(redisCli.publish("ping", System.currentTimeMillis))

  val port = HerokuModule.Port
  val route = logRequestResult("monarchy-web") {
    import AuthFilter._
    pathSingleSlash(new RootController) ~
    path("admin")(AuthRoute(Admin, new AdminController)) ~
    path("graphql")(CorsModule.corsHandler(AuthRoute(All, new GraphqlController))) ~
    path("connect")(AuthRoute(LoggedIn, handleWebSocketMessages(webSocketService)))
  }
  val routeLogged = DebuggingDirectives.logRequestResult("LOG:", Logging.InfoLevel)(route)
  val routeBindings = Http().bindAndHandle(routeLogged, HerokuModule.Host, port)

  println(s"web-server online at port=$port")
}
