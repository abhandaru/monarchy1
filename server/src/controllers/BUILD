load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='controllers',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/com/typesafe/akka:akka_http',
    '//3rdparty/jvm/com/typesafe/akka:akka_http_core',
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//server/src/dal',
  ]
)
