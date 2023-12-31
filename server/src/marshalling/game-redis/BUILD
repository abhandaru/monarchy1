load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='game-redis',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com/typesafe/akka:akka_actor',
    '@maven//:com/github/etaty:rediscala',
    '//server/src/marshalling/game',
    '//server/src/util',
  ]
)
