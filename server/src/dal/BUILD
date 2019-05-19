load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='dal',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  # Anything that depends on this package always needs these ones.
  exports=[
    '//3rdparty/jvm/com/typesafe:config',
    '//3rdparty/jvm/com/typesafe/slick',
    '//3rdparty/jvm/com/github/tminglei:slick_pg',
    '//3rdparty/jvm/com/github/tminglei:slick_pg_core',
  ],
  deps=[
    '//3rdparty/jvm/com/typesafe:config',
    '//3rdparty/jvm/com/typesafe/slick',
    '//3rdparty/jvm/com/github/tminglei:slick_pg',
    '//3rdparty/jvm/com/github/tminglei:slick_pg_core',
    # '//common/util'
  ]
)
