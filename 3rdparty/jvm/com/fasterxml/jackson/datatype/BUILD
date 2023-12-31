load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "jackson_datatype_jsr310",
    exports = [
        "//external:jar/com/fasterxml/jackson/datatype/jackson_datatype_jsr310"
    ],
    runtime_deps = [
        "@maven//:com/fasterxml/jackson/core:jackson_annotations",
        "@maven//:com/fasterxml/jackson/core:jackson_core",
        "@maven//:com/fasterxml/jackson/core:jackson_databind"
    ],
    visibility = [
        "//visibility:public"
    ]
)


