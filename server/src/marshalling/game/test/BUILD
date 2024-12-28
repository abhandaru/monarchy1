load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_test')
scala_test(
  name='test',
  srcs=glob(['*.scala']),
  deps=[
    '@maven//:org_scalatest_scalatest_2_13',
    '//server/src/game',
    '//server/src/marshalling/game',
    '//server/src/test-util',
    '//server/src/util',
  ]
)
