load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "scala_library",
    exports = [
        "//external:jar/org/scala_lang/scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "scala_reflect",
    exports = [
        "//external:jar/org/scala_lang/scala_reflect"
    ],
    runtime_deps = [
        ":scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


