load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='auth',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//3rdparty/jvm/io/jsonwebtoken:jjwt',
    '//server/src/dal',
  ]
)

