load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='core',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com_typesafe_akka_akka_actor_2_12',
    '//server/src/auth',
    '//server/src/game',
  ]
)
