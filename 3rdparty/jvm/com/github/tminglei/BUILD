load("@io_bazel_rules_scala//scala:scala_import.bzl", "scala_import")
scala_import(
    name = "slick_pg",
    jars = [
        "//external:jar/com/github/tminglei/slick_pg_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe/slick:slick",
        "//3rdparty/jvm/org/postgresql:postgresql",
        "//3rdparty/jvm/org/scala_lang:scala_reflect",
        ":slick_pg_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



scala_import(
    name = "slick_pg_core",
    jars = [
        "//external:jar/com/github/tminglei/slick_pg_core_2_12"
    ],
    runtime_deps = [
        "//3rdparty/jvm/com/typesafe/slick:slick",
        "//3rdparty/jvm/org/postgresql:postgresql",
        "//3rdparty/jvm/org/scala_lang:scala_reflect"
    ],
    visibility = [
        "//visibility:public"
    ]
)


