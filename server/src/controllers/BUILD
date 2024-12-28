load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='controllers',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  resources=glob(['*.html']),
  deps=[
    '@maven//:com_typesafe_akka_akka_actor_2_13',
    '@maven//:com_typesafe_akka_akka_http_2_13',
    '@maven//:com_typesafe_akka_akka_http_core_2_13',
    '@maven//:com_typesafe_akka_akka_stream_2_13',
    '@maven//:io_jsonwebtoken_jjwt_api',
    '@maven//:org_sangria_graphql_sangria_2_13',
    '@maven//:org_sangria_graphql_sangria_marshalling_api_2_13',
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/graphql',
    '//server/src/util',
  ]
)

