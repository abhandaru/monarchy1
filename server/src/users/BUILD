load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')

scala_library(
  name='users',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//server/src/auth',
    '//server/src/dal',
    '//server/src/dal-write',
    '//server/src/util',
    '//server/src/util/http',
  ]
)


