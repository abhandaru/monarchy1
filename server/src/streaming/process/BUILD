load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='process',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/dal-write',
    '//server/src/game',
    '//server/src/marshalling/game',
    '//server/src/marshalling/game-redis',
    '//server/src/streaming/core',
    '//server/src/util',
    '@maven//:com_github_etaty_rediscala_2_13',
  ]
)
