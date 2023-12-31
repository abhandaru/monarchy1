load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
  name = "bazel_skylib",
  sha256 = "b8a1527901774180afc798aeb28c4634bdccf19c4d98e7bdd1ce79d1fe9aaad7",
  urls = [
  "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
  "https://github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
  ],
)

# See https://github.com/bazelbuild/rules_scala/releases for up to date version information.
http_archive(
  name = "io_bazel_rules_scala",
  sha256 = "71324bef9bc5a885097e2960d5b8effed63399b55572219919d25f43f468c716",
  strip_prefix = "rules_scala-6.2.1",
  url = "https://github.com/bazelbuild/rules_scala/releases/download/v6.2.1/rules_scala-v6.2.1.tar.gz",
)

load("@io_bazel_rules_scala//:scala_config.bzl", "scala_config")
# Stores Scala version and other configuration
# 2.12 is a default version, other versions can be use by passing them explicitly:
# scala_config(scala_version = "2.11.12")
# Scala 3 requires extras...
#   3.2 should be supported on master. Please note that Scala artifacts for version (3.2.2) are not defined in
#   Rules Scala, they need to be provided by your WORKSPACE. You can use external loader like
#   https://github.com/bazelbuild/rules_jvm_external
scala_config()

load("@io_bazel_rules_scala//scala:scala.bzl", "rules_scala_setup", "rules_scala_toolchain_deps_repositories")

# loads other rules Rules Scala depends on
rules_scala_setup()

# Loads Maven deps like Scala compiler and standard libs. On production projects you should consider
# defining a custom deps toolchains to use your project libs instead
rules_scala_toolchain_deps_repositories(fetch_sources = True)

# load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
# rules_proto_dependencies()
# rules_proto_toolchains()

load("@io_bazel_rules_scala//scala:toolchains.bzl", "scala_register_toolchains")
scala_register_toolchains()

# optional: setup ScalaTest toolchain and dependencies
load("@io_bazel_rules_scala//testing:scalatest.bzl", "scalatest_repositories", "scalatest_toolchain")
scalatest_repositories()
scalatest_toolchain()

http_archive(
  name = "io_bazel_rules_jvm_external",
  urls = ["https://github.com/bazelbuild/rules_jvm_external/archive/3.2.zip"],
  strip_prefix = "rules_jvm_external-3.2",
)

load("@io_bazel_rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
  artifacts = [
    "com.fasterxml.jackson.core:jackson-annotations:2.14.2",
    "com.fasterxml.jackson.core:jackson-core:2.14.2",
    "com.fasterxml.jackson.core:jackson-databind:2.14.2",
    "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2",
    "com.fasterxml.jackson.module:jackson-module-scala_2.12:2.14.2",
    "com.github.etaty:rediscala_2.12:1.8.0",
    "com.github.tminglei:slick-pg_2.12:0.16.1",
    "com.github.tminglei:slick-pg_core_2.12:0.16.1",
    "com.typesafe.akka:akka-actor_2.12:2.7.0",
    "com.typesafe.akka:akka-http-core_2.12:10.4.0",
    "com.typesafe.akka:akka-http_2.12:10.4.0",
    "com.typesafe.akka:akka-stream_2.12:2.7.0",
    "com.typesafe.scala-logging:scala-logging_2.12:3.9.2",
    "com.typesafe.slick:slick-hikaricp_2.12:3.3.3",
    "com.typesafe.slick:slick_2.12:3.3.3",
    "com.typesafe:config:1.4.2",
    "io.jsonwebtoken:jjwt-api:0.11.5",
    "io.jsonwebtoken:jjwt-impl:0.11.5",
    "org.parboiled:parboiled_2.12:2.1.4",
    "org.reactivestreams:reactive-streams:1.0.4",
    "org.sangria-graphql:sangria-marshalling-api_2.12:1.0.3",
    "org.sangria-graphql:sangria_2.12:1.4.2",
  ],
  repositories = [
    "https://repo.maven.apache.org/maven2",
  ],
)
