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