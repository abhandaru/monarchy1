load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "monocle_core_2_12",
    exports = [
        "//external:jar/com/github/julien_truffaut/monocle_core_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scalaz:scalaz_core_2_12"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


