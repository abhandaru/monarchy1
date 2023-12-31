load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "slick",
    jars = [
        "//external:jar/com/typesafe/slick/slick_2_12"
    ],
    runtime_deps = [
        "@maven//:com/typesafe:config",
        "@maven//:org/reactivestreams:reactive_streams",
        "@maven//:org/scala_lang:scala_library",
        "@maven//:org/slf4j:slf4j_api"
    ],
    visibility = [
        "//visibility:public"
    ]
)


