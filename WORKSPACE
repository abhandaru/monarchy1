# update this as needed
RULES_SCALA_VERSION='f3113fb6e9e35cb8f441d2305542026d98afc0a2'
load('@bazel_tools//tools/build_defs/repo:http.bzl', 'http_archive')

http_archive(
  name="com_google_protobuf",
  sha256="9510dd2afc29e7245e9e884336f848c8a6600a14ae726adb6befdb4f786f0be2",
  strip_prefix="protobuf-3.6.1.3",
  urls=["https://github.com/protocolbuffers/protobuf/archive/v3.6.1.3.zip"],
)

http_archive(
  name='io_bazel_rules_scala',
  url='https://github.com/bazelbuild/rules_scala/archive/%s.zip' % RULES_SCALA_VERSION,
  type='zip',
  strip_prefix='rules_scala-%s' % RULES_SCALA_VERSION
)

load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_repositories')
scala_repositories(('2.12.8', {
 'scala_compiler': 'f34e9119f45abd41e85b9e121ba19dd9288b3b4af7f7047e86dc70236708d170',
 'scala_library': '321fb55685635c931eba4bc0d7668349da3f2c09aee2de93a70566066ff25c28',
 'scala_reflect': '4d6405395c4599ce04cea08ba082339e3e42135de9aae2923c9f5367e957315a'
}))

load('@io_bazel_rules_scala//scala:toolchains.bzl', 'scala_register_toolchains')
scala_register_toolchains()

load('//3rdparty:workspace.bzl', 'maven_dependencies')
maven_dependencies()
