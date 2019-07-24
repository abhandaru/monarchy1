load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='controllers',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  resources=glob(['*.html']),
  deps=[
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//3rdparty/jvm/com/typesafe/akka:akka_http',
    '//3rdparty/jvm/com/typesafe/akka:akka_http_core',
    '//3rdparty/jvm/com/typesafe/akka:akka_stream',
    '//3rdparty/jvm/org/sangria_graphql:sangria',
    '//3rdparty/jvm/org/sangria_graphql:sangria_marshalling_api',
    '//server/src/dal',
    '//server/src/graphql',
    '//server/src/util',
  ]
)

