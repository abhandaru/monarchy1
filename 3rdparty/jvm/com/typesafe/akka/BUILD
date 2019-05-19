load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "akka_actor",
    jars = [
        "//external:jar/com/typesafe/akka/akka_actor_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe:config",
        "//3rdparty/jvm/org/scala_lang/modules:scala_java8_compat_2_12",
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "akka_http",
    jars = [
        "//external:jar/com/typesafe/akka/akka_http_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":akka_http_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "akka_http_core",
    jars = [
        "//external:jar/com/typesafe/akka/akka_http_core_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":akka_parsing_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "akka_parsing_2_12",
    exports = [
        "//external:jar/com/typesafe/akka/akka_parsing_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "akka_protobuf_2_12",
    exports = [
        "//external:jar/com/typesafe/akka/akka_protobuf_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/scala_lang:scala_library"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



scala_import(
    name = "akka_stream",
    jars = [
        "//external:jar/com/typesafe/akka/akka_stream_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe:ssl_config_core_2_12",
        "//3rdparty/jvm/org/reactivestreams:reactive_streams",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        ":akka_actor",
        ":akka_protobuf_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)


