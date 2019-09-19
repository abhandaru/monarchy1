load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='game-redis',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//3rdparty/jvm/com/github/etaty:rediscala',
    '//server/src/marshalling/game',
    '//server/src/util',
  ]
)
