load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='controllers',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  resources=glob(['*.html']),
  deps=[
    '@maven//:com/typesafe/akka:akka_actor',
    '@maven//:com/typesafe/akka:akka_http',
    '@maven//:com/typesafe/akka:akka_http_core',
    '@maven//:com/typesafe/akka:akka_stream',
    '@maven//:io/jsonwebtoken:jjwt',
    '@maven//:org/sangria_graphql:sangria',
    '@maven//:org/sangria_graphql:sangria_marshalling_api',
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/graphql',
    '//server/src/util',
  ]
)

