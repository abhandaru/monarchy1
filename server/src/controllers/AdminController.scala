package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._

class AdminController(implicit ec: ExecutionContext) extends GetController {
  override def action(ctx: AuthContext) = {
    Future.successful(HttpResponse(
      StatusCodes.OK,
      entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, AdminController.Html)
    ))
  }
}

object AdminController {
  val Html = """<!DOCTYPE html>
<html lang="en">
  <head>
    <title>monarchy / admin</title>
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/npm/graphiql@0.11.11/graphiql.css" />
    <script src="//cdn.jsdelivr.net/react/15.4.2/react.min.js"></script>
    <script src="//cdn.jsdelivr.net/react/15.4.2/react-dom.min.js"></script>
    <script src="//cdn.jsdelivr.net/npm/graphiql@0.11.11/graphiql.min.js"></script>
  </head>
  <style>
    html, body {
      height: 100%;
      margin: 0;
      overflow: hidden;
      width: 100%;
    }
    .graphiql-container {
      height: 100vh;
    }
  </style>
  <body>
    <div id="graphiql-container"></div>
    <script>
      function graphQLFetcher(graphQLQuery) {
        return fetch(window.location.origin + '/graphql', {
          method: 'post',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(graphQLQuery),
          credentials: 'same-origin'
        }).then(response => response.json());
      }
      ReactDOM.render(
        React.createElement(GraphiQL, { fetcher: graphQLFetcher}),
        document.getElementById('graphiql-container')
      );
    </script>
  </body>
</html>"""
}
