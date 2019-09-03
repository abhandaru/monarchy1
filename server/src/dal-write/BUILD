load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='dal',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '//server/src/dal'
  ]
)
