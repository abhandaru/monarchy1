load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='format',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com/github/etaty:rediscala',
    '//server/src/dal',
    '//server/src/game',
    '//server/src/marshalling/game-redis',
    '//server/src/streaming/core',
    '//server/src/util',
  ]
)
