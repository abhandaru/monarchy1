load('@io_bazel_rules_scala//scala:scala.bzl', 'scala_binary')
scala_binary(
  name='web',
  srcs=glob(['*.scala']),
  main_class='monarchy.web.WebServer',
  deps=[
    '//3rdparty/jvm/com/typesafe/akka:akka_actor',
    '//3rdparty/jvm/com/typesafe/akka:akka_http',
    '//3rdparty/jvm/com/typesafe/akka:akka_http_core',
    '//3rdparty/jvm/com/typesafe/akka:akka_stream',
    '//server/src/dal',
  ]
)
