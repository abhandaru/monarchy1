load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "circe_core",
    jars = [
        "//external:jar/io/circe/circe_core_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/typelevel:cats_core",
        ":circe_numbers_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "circe_jawn",
    jars = [
        "//external:jar/io/circe/circe_jawn_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/spire_math:jawn_parser_2_12",
        ":circe_core"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "circe_numbers_2_12",
    exports = [
        "//external:jar/io/circe/circe_numbers_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "circe_optics",
    jars = [
        "//external:jar/io/circe/circe_optics_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/github/julien_truffaut:monocle_core",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":circe_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "circe_parser",
    jars = [
        "//external:jar/io/circe/circe_parser_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":circe_core",
        ":circe_jawn"
    ],
    visibility = [
        "//visibility:public"
    ]
)


