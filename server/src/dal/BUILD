load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='dal',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  # Anything that depends on this package always needs these ones.
  exports=[
    '@maven//:com_typesafe_config',
    '@maven//:com_typesafe_slick_slick_2_13',
    '@maven//:com_github_tminglei_slick_pg_2_13',
    '@maven//:com_github_tminglei_slick_pg_core_2_13',
  ],
  deps=[
    '@maven//:com_typesafe_config',
    '@maven//:com_typesafe_slick_slick_2_13',
    '@maven//:com_github_tminglei_slick_pg_2_13',
    '@maven//:com_github_tminglei_slick_pg_core_2_13',
  ]
)
