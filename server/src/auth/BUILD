load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='auth',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:io_jsonwebtoken_jjwt_api',
    '@maven//:io_jsonwebtoken_jjwt_impl',
    '@maven//:io_jsonwebtoken_jjwt_jackson',
    '//server/src/dal',
  ]
)

