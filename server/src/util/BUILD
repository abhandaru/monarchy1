load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='util',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    '@maven//:com_fasterxml_jackson_core_jackson_annotations',
    '@maven//:com_fasterxml_jackson_core_jackson_core',
    '@maven//:com_fasterxml_jackson_core_jackson_databind',
    '@maven//:com_fasterxml_jackson_datatype_jackson_datatype_jsr310',
    '@maven//:com_fasterxml_jackson_module_jackson_module_scala_2_12',
  ],
  exports=[
    '@maven//:com_fasterxml_jackson_core_jackson_annotations',
    '@maven//:com_fasterxml_jackson_core_jackson_core',
    '@maven//:com_fasterxml_jackson_core_jackson_databind',
    '@maven//:com_fasterxml_jackson_datatype_jackson_datatype_jsr310',
    '@maven//:com_fasterxml_jackson_module_jackson_module_scala_2_12',
    # logging exports
    '@maven//:com_typesafe_akka_akka_slf4j_2_12',
    '@maven//:com_typesafe_scala_logging_scala_logging_2_12',
    '@maven//:org_slf4j_slf4j_api',
  ]
)
