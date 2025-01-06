package monarchy.web

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import monarchy.auth.AuthFilter
import monarchy.auth.oauth2.ExchangeClient
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
  implicit val exchangeCli: ExchangeClient = DiscordModule.exchangeClient

  // Request handlers
  import AuthFilter._
  import Directives._

  def public(c: AuthController): Route =
    AuthRoute(All, c)

  // val statusController = new StatusController
  val statusController = public(new StatusController)
  val adminController = AuthRoute(Admin, new AdminController)
  val graphqlController = CorsModule.corsHandler(public(new GraphqlController))
  val connectController = AuthRoute(LoggedIn, { c =>
    val messageFlow = MessageTopologyBuilder(RedisModule.RedisSocketAddr, c.auth).build
    handleWebSocketMessages(messageFlow)(c.request)
  })

  val port = CloudModule.Port
  val route = logRequestResult("monarchy-web") {
    pathSingleSlash(statusController) ~
      path("admin")(adminController) ~
      path("connect")(connectController) ~
      path("graphql")(graphqlController) ~
      path("healthz")(statusController) ~
      path("oauth2" / "discord" / "authorize")(public(new DiscordAuthorizeController)) ~
      path("oauth2" / "discord" / "exchange")(public(new DiscordExchangeController))
  }
  val routeLogged = LoggingModule.log(route)
  val routeBindings = Http().bindAndHandle(routeLogged, CloudModule.Host, port)

  println(s"[monarchy-web] online at port=$port")
}
