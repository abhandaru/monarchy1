load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='format',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com_github_etaty_rediscala_2_13',
    '//server/src/dal',
    '//server/src/game',
    '//server/src/marshalling/game-redis',
    '//server/src/streaming/core',
    '//server/src/util',
  ]
)
