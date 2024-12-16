load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_macro_library')

scala_macro_library(
  name = "util-macros",
  srcs = glob(["*.scala"]),
  visibility = ["//visibility:public"],
  # Set this flag for macro debugging
  # scalacopts=['-Ymacro-debug-lite']
)
