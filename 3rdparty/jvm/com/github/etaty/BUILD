load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "rediscala",
    jars = [
        "//external:jar/com/github/etaty/rediscala_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe/akka:akka_actor",
        "//3rdparty/jvm/org/scala_lang:scala_library",
        "//3rdparty/jvm/org/scala_stm:scala_stm_2_12"
    ],
    visibility = [
        "//visibility:public"
    ]
)


