load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
java_library(
    name = "macro_visit_2_12",
    exports = [
        "//external:jar/org/sangria_graphql/macro_visit_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "sangria",
    jars = [
        "//external:jar/org/sangria_graphql/sangria_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/parboiled:parboiled",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_lang:scala_reflect",
        ":macro_visit_2_12",
        ":sangria_marshalling_api",
        ":sangria_streaming_api_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "sangria_marshalling_api",
    jars = [
        "//external:jar/org/sangria_graphql/sangria_marshalling_api_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "sangria_streaming_api_2_12",
    exports = [
        "//external:jar/org/sangria_graphql/sangria_streaming_api_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


