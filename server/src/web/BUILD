load('@aspect_bazel_lib//lib:tar.bzl', 'tar')
load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_binary')
load('@rules_oci//oci:defs.bzl', 'oci_image', 'oci_push')

scala_binary(
  name='web',
  srcs=glob(['*.scala']),
  main_class='monarchy.web.WebServer',
  deps=[
    '@maven//:com_typesafe_akka_akka_actor_2_13',
    '@maven//:com_typesafe_akka_akka_http_2_13',
    '@maven//:com_typesafe_akka_akka_http_core_2_13',
    '@maven//:com_typesafe_akka_akka_stream_2_13',
    '@maven//:com_github_etaty_rediscala_2_13',
    '@maven//:org_reactivestreams_reactive_streams',
    '//server/src/auth',
    '//server/src/controllers',
    '//server/src/dal',
    '//server/src/graphql',
    '//server/src/streaming/core',
    '//server/src/streaming/format',
    '//server/src/streaming/process',
    '//server/src/streaming/topology',
    '//server/src/util',
  ]
)

tar(
  name = 'web-layer',
  srcs = [':web_deploy.jar'],
)

oci_image(
  name = 'web-image',
  base = '@distroless_java',
  entrypoint = ['java', '-jar', '/app/web_deploy.jar'],
  tars = [':web-layer'],
)

oci_push(
  name = 'web-image-push',
  image = ':web-image',
  repository = '930597685973.dkr.ecr.us-east-1.amazonaws.com/monarchy-api',
  remote_tags = ['latest']
)
