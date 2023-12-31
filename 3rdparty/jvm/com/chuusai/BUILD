load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "shapeless_2_12",
    exports = [
        "//external:jar/com/chuusai/shapeless_2_12"
    ],
    runtime_deps = [
        "@maven//:org/scala_lang:scala_library",
        "@maven//:org/typelevel:macro_compat_2_12"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


