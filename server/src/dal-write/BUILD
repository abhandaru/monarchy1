load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='dal-write',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:org_scala_lang_modules_scala_collection_compat_2_12',
    '//server/src/dal'
  ]
)
