load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='game-redis',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com_typesafe_akka_akka_actor_2_12',
    '@maven//:com_github_etaty_rediscala_2_12',
    '//server/src/marshalling/game',
    '//server/src/util',
  ]
)
