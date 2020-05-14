scala_version='2.12.6'
rules_scala_version='a676633dc14d8239569affb2acafbef255df3480' # update this as needed
load('@bazel_tools//tools/build_defs/repo:http.bzl', 'http_archive')
http_archive(
  name='io_bazel_rules_scala',
  url='https://github.com/bazelbuild/rules_scala/archive/%s.zip' % rules_scala_version,
  type='zip',
  strip_prefix='rules_scala-%s' % rules_scala_version
)
http_archive(
  name='rules_proto',
  sha256='57001a3b33ec690a175cdf0698243431ef27233017b9bed23f96d44b9c98242f',
  strip_prefix='rules_proto-9cd4f8f1ede19d81c6d48910429fe96776e567b1',
  urls=[
    'https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/9cd4f8f1ede19d81c6d48910429fe96776e567b1.tar.gz',
    'https://github.com/bazelbuild/rules_proto/archive/9cd4f8f1ede19d81c6d48910429fe96776e567b1.tar.gz',
  ],
)
# bazel-skylib 0.8.0 released 2019.03.20 (https://github.com/bazelbuild/bazel-skylib/releases/tag/0.8.0)
skylib_version = "0.8.0"
http_archive(
  name = "bazel_skylib",
  type = "tar.gz",
  url = "https://github.com/bazelbuild/bazel-skylib/releases/download/{}/bazel-skylib.{}.tar.gz".format (skylib_version, skylib_version),
  sha256 = "2ef429f5d7ce7111263289644d233707dba35e39696377ebab8b0bc701f7818e",
)
load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_repositories')
scala_repositories((scala_version, {
  'scala_compiler': '3023b07cc02f2b0217b2c04f8e636b396130b3a8544a8dfad498a19c3e57a863',
  'scala_library': 'f81d7144f0ce1b8123335b72ba39003c4be2870767aca15dd0888ba3dab65e98',
  'scala_reflect': 'ffa70d522fc9f9deec14358aa674e6dd75c9dfa39d4668ef15bb52f002ce99fa'
}))
load('@io_bazel_rules_scala//scala:toolchains.bzl', 'scala_register_toolchains')
scala_register_toolchains()
load('@io_bazel_rules_scala//scala_proto:scala_proto.bzl', 'scala_proto_repositories')
scala_proto_repositories(scala_version=scala_version)
load('@io_bazel_rules_scala//scala_proto:toolchains.bzl', 'scala_proto_register_toolchains')
scala_proto_register_toolchains()
load('@rules_proto//proto:repositories.bzl', 'rules_proto_dependencies', 'rules_proto_toolchains')
rules_proto_dependencies()
rules_proto_toolchains()
load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")
bazel_skylib_workspace()
load('//3rdparty:workspace.bzl', 'maven_dependencies')
maven_dependencies()