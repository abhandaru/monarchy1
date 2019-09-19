load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_test')
scala_test(
  name='test',
  srcs=glob(['*.scala']),
  deps=[
    '//server/src/game',
    '//server/src/marshalling/game',
    '//server/src/util',
  ]
)
