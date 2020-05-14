package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._

class GraphiqlController(implicit ec: ExecutionContext) extends GetController {
  override def action(ctx: AuthContext) = {
    Future.successful(HttpResponse(
      StatusCodes.OK,
      entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, GraphiqlController.Html)
    ))
  }
}
//TODO: need MIT license?
object GraphiqlController {
  val Html = """<!DOCTYPE html>
<html lang="en">
  <head>
    <title>GraphiQL Admin Console</title>
    <link rel="stylesheet" type="text/css" href="https://unpkg.com/graphiql/graphiql.min.css" rel="stylesheet"/>
    <script crossorigin src="https://unpkg.com/react/umd/react.production.min.js"></script>
    <script crossorigin src="https://unpkg.com/react-dom/umd/react-dom.production.min.js"></script>
    <script crossorigin src="https://unpkg.com/graphiql/graphiql.min.js"></script>
  </head>
  <body>
    <div id="graphiql-container" style="height: 100vh;"></div>

    <script>
      const graphQLFetcher = graphQLParams =>
        fetch(window.origin + '/graphql', {
          method: 'post',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(graphQLParams),
          credentials: 'same-origin'
        }).then(response => response.json())
      ReactDOM.render(
        React.createElement(GraphiQL, { fetcher: graphQLFetcher }),
        document.getElementById('graphiql-container'),
      );
    </script>
  </body>
</html>"""
}
