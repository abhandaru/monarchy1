package monarchy.util.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import monarchy.util.Json
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.runtime.universe.TypeTag
import scala.util.Try

class HttpClient(
    implicit
    system: ActorSystem,
    mat: ActorMaterializer,
    ec: ExecutionContext
) {
  import HttpClient._
  private val http = Http()

  def apply(request: Request): Future[Response] = {
    val fetch = http.singleRequest(HttpRequest(
      method = request.method,
      uri = request.url,
      headers = formatHeaders(request.headers),
      entity = formatBody(request)
    ))
    for {
      res <- fetch
      body <- unmarshal(res)
    } yield Response(res, body)
  }

  private def unmarshal(response: HttpResponse): Future[String] =
    response.entity.dataBytes.runReduce(_ ++ _).map(_.utf8String)
}


object HttpClient {
  case class Request(
      url: String,
      method: HttpMethod = HttpMethods.GET,
      headers: Map[String, String] = Map.empty,
      body: Option[AnyRef] = None
  ) {

    def withAccept(contentType: String): Request =
      copy(headers = headers + ("Accept" -> contentType))

    def withAcceptJson: Request =
      withAccept("application/json")

    def withContentType(contentType: String): Request =
      copy(headers = headers + ("Content-Type" -> contentType))

    def withContentTypeJson: Request =
      withContentType("application/json")

    def asPost: Request =
      copy(method = HttpMethods.POST)
  }

  case class Response(core: HttpResponse, body: String) {
    def status: StatusCode =
      core.status

    def as[A: TypeTag]: Try[A] =
      Json.parseAttempt[A](body)
  }

  // For now just filter out the invalid headers.
  private def formatHeaders(headers: Map[String, String]): List[HttpHeader] = {
    headers.flatMap { case (name, value) => 
      Option(HttpHeader.parse(name, value))
        .collect { case HttpHeader.ParsingResult.Ok(header, _) => header }
    }.toList
  }

  // Add more special cases as needed.
  private def formatBody(req: Request): RequestEntity = {
    req.body match {
      case Some(e: HttpEntity.Strict) => e
      case Some(s: String) => HttpEntity(ContentTypes.`application/json`, s)
      case Some(d: FormData) => d.toEntity
      case Some(ref) => HttpEntity(ContentTypes.`application/json`, Json.stringify(ref))
      case None => HttpEntity.Empty
    }
  }
}
