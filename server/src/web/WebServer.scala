package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import monarchy.auth.AuthFilter
import monarchy.controllers._
import monarchy.dal.QueryClient
import monarchy.graphql.GraphqlContext
import monarchy.streaming.format.ActionRendererProxy
import monarchy.streaming.process.ClientActionProxy
import monarchy.streaming.topology.MessageTopologyBuilder
import redis.RedisClient
import scala.concurrent.ExecutionContext

object WebServer extends App {
  implicit val actorSys: ActorSystem = ActorSystem("monarchy-web")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = actorSys.dispatcher
  implicit val queryCli: QueryClient = DatabaseModule.queryClient
  implicit val redisCli: RedisClient = RedisModule.redisClient
  implicit val graphqlContext: GraphqlContext = GraphqlModule.graphqlContext
  implicit val axnRendererProxy: ActionRendererProxy = StreamingModule.actionRendererProxy
  implicit val clientAxnProxy: ClientActionProxy = StreamingModule.clientActionProxy

  // Request handlers
  import AuthFilter._
  // val statusController = new StatusController
  val statusController = AuthRoute(All, new StatusController)
  val adminController = AuthRoute(Admin, new AdminController)
  val graphqlController = CorsModule.corsHandler(AuthRoute(All, new GraphqlController))
  val connectController = AuthRoute(LoggedIn, { c =>
    val messageFlow = MessageTopologyBuilder(RedisModule.RedisSocketAddr, c.auth).build
    handleWebSocketMessages(messageFlow)(c.request)
  })

  val port = HerokuModule.Port
  val route = logRequestResult("monarchy-web") {
    pathSingleSlash(statusController) ~
      path("healthz")(statusController) ~
      path("admin")(adminController) ~
      path("graphql")(graphqlController) ~
      path("connect")(connectController)
  }
  val routeLogged = LoggingModule.log(route)
  val routeBindings = Http().bindAndHandle(routeLogged, HerokuModule.Host, port)

  println(s"[monarchy-web] online at port=$port")
}
