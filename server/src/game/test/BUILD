load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_test')
scala_test(
  name='test',
  srcs=glob(['*.scala']),
  deps=[
    '@maven//:org_scalactic_scalactic_2_12',
    '@maven//:org_scalatest_scalatest_2_12',
    '//server/src/game'
  ]
)
