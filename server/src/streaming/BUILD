load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='streaming',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//3rdparty/jvm/com/typesafe/akka:akka_stream',
    '//3rdparty/jvm/com/typesafe/akka:akka_http_core',
    '//3rdparty/jvm/com/github/etaty:rediscala',
    '//3rdparty/jvm/org/reactivestreams:reactive_streams',
    '//server/src/util',
  ]
)
