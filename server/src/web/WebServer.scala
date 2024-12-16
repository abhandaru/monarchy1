package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import monarchy.auth.AuthFilter
import monarchy.controllers._
import monarchy.streaming.topology.MessageTopologyBuilder

object WebServer extends App {
  implicit val actorSys = ActorSystem("monarchy-web")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSys.dispatcher
  implicit val queryCli = DatabaseModule.queryClient
  implicit val redisCli = RedisModule.redisClient
  implicit val graphqlContext = GraphqlModule.graphqlContext
  implicit val streamAxnRenderer = StreamingModule.streamActionRenderer
  implicit val clientAxnProxy = StreamingModule.clientActionProxy

  // Request handlers
  import AuthFilter._
  // val statusController = new StatusController
  val rootController = AuthRoute(All, new RootController)
  val adminController = AuthRoute(Admin, new AdminController)
  val graphqlController = CorsModule.corsHandler(AuthRoute(All, new GraphqlController))
  val connectController = AuthRoute(LoggedIn, { c =>
    val messageFlow = MessageTopologyBuilder(RedisModule.RedisSocketAddr, c.auth).build
    handleWebSocketMessages(messageFlow)(c.request)
  })

  val port = HerokuModule.Port
  val route = logRequestResult("monarchy-web") {
    pathSingleSlash(rootController) ~
      path("admin")(adminController) ~
      path("graphql")(graphqlController) ~
      path("connect")(connectController)
  }
  val routeLogged = LoggingModule.log(route)
  val routeBindings = Http().bindAndHandle(routeLogged, HerokuModule.Host, port)

  println(s"[monarchy-web] online at port=$port")
}
