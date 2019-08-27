package monarchy.web

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Directive0, Route}
import monarchy.controllers.AuthRoute
import scala.concurrent.duration._

/**
  * From https://dzone.com/articles/handling-cors-in-akka-http
  * and https://ali.actor
  */
object CorsModule {
  val AllowHeaders = List("Content-Type", AuthRoute.UserBearerKey, AuthRoute.UserIdKey)
  val AllowOrigins = HttpOriginRange(HttpOrigin("http://localhost:8081"))

  val ResponseHeaders = List(
    `Access-Control-Allow-Origin`(AllowOrigins),
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`(AllowHeaders),
    `Access-Control-Max-Age`(1.day.toMillis) //Tell browser to cache OPTIONS requests
  )

  // Adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(ResponseHeaders)
  }

  // Handles preflight OPTIONS requests.
  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK)
      .withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCorsHeaders(response: HttpResponse): HttpResponse =
    response.withHeaders(ResponseHeaders)
}
