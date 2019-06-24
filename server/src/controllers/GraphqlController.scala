package monarchy.controllers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import io.circe._
import io.circe.optics.JsonPath._
import io.circe.parser._
import monarchy.graphql.GraphqlRequestUnmarshaller._
import sangria.ast.Document
import sangria.execution.deferred.DeferredResolver
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.DeliveryScheme.Try
import sangria.parser.{QueryParser, SyntaxError}
import scala.concurrent.{Future, ExecutionContext}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

class GraphqlController(implicit
  ec: ExecutionContext,
  sys: ActorSystem
) extends Controller {
  override def action(ctx: RequestContext) = {
    println(ctx)
    ???
    // entity(as[Json]) { body ⇒
    //   val query = queryParam orElse root.query.string.getOption(body)
    //   val operationName = operationNameParam orElse root.operationName.string.getOption(body)
    //   val variablesStr = variablesParam orElse root.variables.string.getOption(body)
    //   query.map(QueryParser.parse(_)) match {
    //     case Some(Success(ast)) ⇒
    //       variablesStr.map(parse) match {
    //         case Some(Left(error)) ⇒ complete(BadRequest, formatError(error))
    //         case Some(Right(json)) ⇒ executeGraphQL(ast, operationName, json, tracing.isDefined)
    //         case None ⇒ executeGraphQL(ast, operationName, root.variables.json.getOption(body) getOrElse Json.obj(), tracing.isDefined)
    //       }
    //     case Some(Failure(error)) ⇒ complete(BadRequest, formatError(error))
    //     case None ⇒ complete(BadRequest, formatError("No query to execute"))
    //   }
    // } ~
    // entity(as[Document]) { document ⇒
    //   variablesParam.map(parse) match {
    //     case Some(Left(error)) ⇒ complete(BadRequest, formatError(error))
    //     case Some(Right(json)) ⇒ executeGraphQL(document, operationNameParam, json, tracing.isDefined)
    //     case None ⇒ executeGraphQL(document, operationNameParam, Json.obj(), tracing.isDefined)
    //   }
    // }
  }

  def executeGraphQL(query: Document, operationName: Option[String], variables: Json, tracing: Boolean) = {
    ???
    // complete(Executor.execute(SchemaDefinition.StarWarsSchema, query, new CharacterRepo,
    //   variables = if (variables.isNull) Json.obj() else variables,
    //   operationName = operationName,
    //   middleware = if (tracing) SlowLog.apolloTracing :: Nil else Nil,
    //   deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters))
    //     .map(OK -> _)
    //     .recover {
    //       case error: QueryAnalysisError => BadRequest -> error.resolveError
    //       case error: ErrorWithResolver => InternalServerError -> error.resolveError
    //     }
    // )
  }

  def formatError(error: Throwable): Json = error match {
    case syntaxError: SyntaxError =>
      Json.obj("errors" -> Json.arr(
      Json.obj(
        "message" -> Json.fromString(syntaxError.getMessage),
        "locations" -> Json.arr(Json.obj(
          "line" -> Json.fromBigInt(syntaxError.originalError.position.line),
          "column" -> Json.fromBigInt(syntaxError.originalError.position.column))))))
    case NonFatal(e) => formatError(e.getMessage)
    case e => throw e
  }

  def formatError(message: String): Json = {
    Json.obj("errors" -> Json.arr(Json.obj("message" -> Json.fromString(message))))
  }
}
