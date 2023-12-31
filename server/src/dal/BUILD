load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='dal',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  # Anything that depends on this package always needs these ones.
  exports=[
    '@maven//:com/typesafe:config',
    '@maven//:com/typesafe/slick',
    '@maven//:com/github/tminglei:slick_pg',
    '@maven//:com/github/tminglei:slick_pg_core',
  ],
  deps=[
    '@maven//:com/typesafe:config',
    '@maven//:com/typesafe/slick',
    '@maven//:com/github/tminglei:slick_pg',
    '@maven//:com/github/tminglei:slick_pg_core',
  ]
)
