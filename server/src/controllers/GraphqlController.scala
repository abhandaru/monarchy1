package monarchy.controllers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import monarchy.graphql.{GraphqlContext, GraphqlSchema}
import monarchy.util.Json
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.InputUnmarshaller
import sangria.parser.{QueryParser, SyntaxError}
import scala.concurrent.{Future, ExecutionContext}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

case class GraphqlBody(
  query: String,
  operationName: Option[String],
  variables: Option[Map[String, Any]]
)

case class GraphqlErrors(errors: Seq[GraphqlError] = Nil)

case class GraphqlError(
  message: String,
  locations: Seq[GraphqlLocation] = Nil
)

case class GraphqlLocation(
  line: Int,
  column: Int
)

class GraphqlController(implicit
  ec: ExecutionContext,
  sys: ActorSystem,
  gqlContext: GraphqlContext
) extends PostController[String] {
  override def action(ctx: AuthContext, gqlRaw: String) = {
    val gql = Json.parse[GraphqlBody](gqlRaw)
    val execReq = QueryParser.parse(gql.query) match {
      case Failure(e) => Future.successful(BadRequest -> formatError(e))
      case Success(ast) =>
        val variables = gql.variables.getOrElse(Map.empty)
        executeGraphQL(ast, gql.operationName, variables)
    }
    execReq.map {
      case (status, r) =>
        val entity = HttpEntity(ContentTypes.`application/json`, Json.stringify(r))
        HttpResponse(status, entity = entity)
    }
  }

  def executeGraphQL(query: Document, opName: Option[String], vars: Map[String, Any]) = {
    Executor.execute(
      schema = GraphqlSchema,
      queryAst = query,
      userContext = gqlContext,
      variables = InputUnmarshaller.mapVars(vars),
      operationName = opName
    )
    .map(OK -> _)
    .recover {
      case e: QueryAnalysisError => BadRequest -> e.resolveError
      case e: ErrorWithResolver => InternalServerError -> e.resolveError
    }
  }

  def formatError(error: Throwable): GraphqlErrors = error match {
    case e: SyntaxError =>
      GraphqlErrors(Seq(
        GraphqlError(
          message = e.getMessage,
          locations = Seq(GraphqlLocation(
            e.originalError.position.line,
            e.originalError.position.column
          ))
        )
      ))
    case NonFatal(e) => GraphqlErrors(Seq(GraphqlError(message = e.getMessage)))
    case e => throw e
  }
}
