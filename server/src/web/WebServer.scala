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
import monarchy.users.DiscordFlow
import monarchy.util.http.HttpClient
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
  implicit val httpCli: HttpClient = new HttpClient
  implicit val exchangeCli: ExchangeClient = DiscordModule.exchangeClient
  implicit val discordFlow: DiscordFlow = DiscordModule.discordFlow

  // Request handlers
  import AuthFilter._
  import Directives._

  // Route decorators
  def public(c: AuthController): Route = AuthRoute(All, c)
  def loggedIn(c: AuthController): Route = AuthRoute(LoggedIn, c)

  // Routes
  val statusController = public(new StatusController)
  val adminController = AuthRoute(Admin, new AdminController)
  val graphqlController = CorsModule.corsHandler(loggedIn(new GraphqlController))
  val connectController = loggedIn({ c =>
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
