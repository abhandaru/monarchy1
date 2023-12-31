load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "jjwt",
    exports = [
        "//external:jar/io/jsonwebtoken/jjwt"
    ],
    runtime_deps = [
        "@maven//:com/fasterxml/jackson/core:jackson_databind"
    ],
    visibility = [
        "//visibility:public"
    ]
)


