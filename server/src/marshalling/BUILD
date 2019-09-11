load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_library')
scala_library(
  name='marshalling',
  srcs=glob(['*.scala']),
  visibility=['//visibility:public'],
  deps=[
    # Can remove these if we move to another serialization strategy
    '//3rdparty/jvm/com/fasterxml/jackson/core:jackson_annotations',
    '//3rdparty/jvm/com/fasterxml/jackson/core:jackson_core',
    '//3rdparty/jvm/com/fasterxml/jackson/core:jackson_databind',
    '//3rdparty/jvm/com/fasterxml/jackson/datatype:jackson_datatype_jsr310',
    '//3rdparty/jvm/com/fasterxml/jackson/module:jackson_module_scala',
    # Core marshalling dependencies
    '//server/src/game',
    '//server/src/util',
  ]
)
