load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='graphql',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/org/sangria_graphql:sangria',
    '//3rdparty/jvm/org/sangria_graphql:sangria_marshalling_api',
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/game',
    '//server/src/util',
  ],
  exports=[
    '//3rdparty/jvm/org/parboiled',
  ]
)
