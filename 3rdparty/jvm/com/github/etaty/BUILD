load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "rediscala",
    jars = [
        "//external:jar/com/github/etaty/rediscala_2_12"
    ],
    runtime_deps = [
        "@maven//:com/typesafe/akka:akka_actor",
        "@maven//:org/scala_lang:scala_library",
        "@maven//:org/scala_stm:scala_stm_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)


