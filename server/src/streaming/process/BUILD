load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='process',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com/github/etaty:rediscala',
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/dal-write',
    '//server/src/game',
    '//server/src/streaming/core',
    '//server/src/marshalling/game',
    '//server/src/marshalling/game-redis',
    '//server/src/util',
  ]
)
