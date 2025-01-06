load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')

scala_library(
  name='auth-oauth2',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com_typesafe_akka_akka_actor_2_13',
    '@maven//:com_typesafe_akka_akka_http_core_2_13',
    '@maven//:com_typesafe_akka_akka_parsing_2_13',
    '@maven//:com_typesafe_akka_akka_stream_2_13',
    '//server/src/util',
  ]
)

