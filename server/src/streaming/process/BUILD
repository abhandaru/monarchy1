load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='process',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/com/github/etaty:rediscala',
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/dal-write',
    '//server/src/game',
    '//server/src/streaming/core',
    '//server/src/marshalling',
    '//server/src/util',
  ]
)
