load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "slick",
    jars = [
        "//external:jar/com/typesafe/slick/slick_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe:config",
        "//3rdparty/jvm/org/reactivestreams:reactive_streams",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/slf4j:slf4j_api"
    ],
    visibility = [
        "//visibility:public"
    ]
)


