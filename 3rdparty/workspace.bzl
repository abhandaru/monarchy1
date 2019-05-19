# Do not edit. bazel-deps autogenerates this file from dependencies.yaml.
def _jar_artifact_impl(ctx):
    jar_name = "%s.jar" % ctx.name
    ctx.download(
        output=ctx.path("jar/%s" % jar_name),
        url=ctx.attr.urls,
        sha256=ctx.attr.sha256,
        executable=False
    )
    src_name="%s-sources.jar" % ctx.name
    srcjar_attr=""
    has_sources = len(ctx.attr.src_urls) != 0
    if has_sources:
        ctx.download(
            output=ctx.path("jar/%s" % src_name),
            url=ctx.attr.src_urls,
            sha256=ctx.attr.src_sha256,
            executable=False
        )
        srcjar_attr ='\n    srcjar = ":%s",' % src_name

    build_file_contents = """
package(default_visibility = ['//visibility:public'])
java_import(
    name = 'jar',
    tags = ['maven_coordinates={artifact}'],
    jars = ['{jar_name}'],{srcjar_attr}
)
filegroup(
    name = 'file',
    srcs = [
        '{jar_name}',
        '{src_name}'
    ],
    visibility = ['//visibility:public']
)\n""".format(artifact = ctx.attr.artifact, jar_name = jar_name, src_name = src_name, srcjar_attr = srcjar_attr)
    ctx.file(ctx.path("jar/BUILD"), build_file_contents, False)
    return None

jar_artifact = repository_rule(
    attrs = {
        "artifact": attr.string(mandatory = True),
        "sha256": attr.string(mandatory = True),
        "urls": attr.string_list(mandatory = True),
        "src_sha256": attr.string(mandatory = False, default=""),
        "src_urls": attr.string_list(mandatory = False, default=[]),
    },
    implementation = _jar_artifact_impl
)

def jar_artifact_callback(hash):
    src_urls = []
    src_sha256 = ""
    source=hash.get("source", None)
    if source != None:
        src_urls = [source["url"]]
        src_sha256 = source["sha256"]
    jar_artifact(
        artifact = hash["artifact"],
        name = hash["name"],
        urls = [hash["url"]],
        sha256 = hash["sha256"],
        src_urls = src_urls,
        src_sha256 = src_sha256
    )
    native.bind(name = hash["bind"], actual = hash["actual"])


def list_dependencies():
    return [
    {"artifact": "com.typesafe.akka:akka-actor_2.12:2.5.19", "lang": "scala", "sha1": "24707fdb38b1d8e74ccfe816f9fa145cf8c097e3", "sha256": "33b5eef9db3766a7e7a307457995fde116967ead482c88377b2466475d8f703b", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-actor_2.12/2.5.19/akka-actor_2.12-2.5.19.jar", "source": {"sha1": "c082477c7efba8de52d95ccdc7cedb6c680bf3af", "sha256": "5f4a9a2b8e8f258de65c7b7beeaf5814e7b946e2a82d947c2de3ef9f15f55ea5", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-actor_2.12/2.5.19/akka-actor_2.12-2.5.19-sources.jar"} , "name": "com_typesafe_akka_akka_actor_2_12", "actual": "@com_typesafe_akka_akka_actor_2_12//jar:file", "bind": "jar/com/typesafe/akka/akka_actor_2_12"},
    {"artifact": "com.typesafe.akka:akka-http-core_2.12:10.1.8", "lang": "scala", "sha1": "824947965b76cfd3024b9cba41632c5f67768701", "sha256": "d456d457613db43fe10c1bfd0e5046672061eba2af802c81970a470ec397014f", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-http-core_2.12/10.1.8/akka-http-core_2.12-10.1.8.jar", "source": {"sha1": "dbeaae3816245edfab1bbad2be5f523f112da3b6", "sha256": "c1bb96f732bc07e5c7a52f785de2212ae33eaa3cf25a3796743b3c77046bf668", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-http-core_2.12/10.1.8/akka-http-core_2.12-10.1.8-sources.jar"} , "name": "com_typesafe_akka_akka_http_core_2_12", "actual": "@com_typesafe_akka_akka_http_core_2_12//jar:file", "bind": "jar/com/typesafe/akka/akka_http_core_2_12"},
    {"artifact": "com.typesafe.akka:akka-http_2.12:10.1.8", "lang": "scala", "sha1": "666012460071d248f77018586dc6c22468a39a15", "sha256": "40f8457b6011dda35edcd30911c55d086800d0dad065e3d91c9b4190522aa824", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-http_2.12/10.1.8/akka-http_2.12-10.1.8.jar", "source": {"sha1": "88a7d039921da654a563e26ad76ec8b0e61480d8", "sha256": "d43839b0b69d801cdbd2bd1637ae01b7bd853fac6d75108af8a61ec2359c2424", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-http_2.12/10.1.8/akka-http_2.12-10.1.8-sources.jar"} , "name": "com_typesafe_akka_akka_http_2_12", "actual": "@com_typesafe_akka_akka_http_2_12//jar:file", "bind": "jar/com/typesafe/akka/akka_http_2_12"},
    {"artifact": "com.typesafe.akka:akka-parsing_2.12:10.1.8", "lang": "java", "sha1": "f719744e99a5c6a629e0560ef4e5f2d513caa302", "sha256": "ef104a616c9acf498e80cc643a73bde4cd67821ec12f48676ad4aec017201174", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-parsing_2.12/10.1.8/akka-parsing_2.12-10.1.8.jar", "source": {"sha1": "6d14a2f9a035e8f1adec20566968cf1c55ad79dd", "sha256": "a833b50687cac32ba4fdba19a5146df04b7a8f95d325fc1229a6960d5a81e274", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-parsing_2.12/10.1.8/akka-parsing_2.12-10.1.8-sources.jar"} , "name": "com_typesafe_akka_akka_parsing_2_12", "actual": "@com_typesafe_akka_akka_parsing_2_12//jar", "bind": "jar/com/typesafe/akka/akka_parsing_2_12"},
    {"artifact": "com.typesafe.akka:akka-protobuf_2.12:2.5.19", "lang": "java", "sha1": "c96077a5f90e97609368b2bfb6916dd1d7d30ef7", "sha256": "148401ab94c9b95efb4ac9f9bba3915b53c72bb0a05e26b3d5b00bb3dcf80cee", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-protobuf_2.12/2.5.19/akka-protobuf_2.12-2.5.19.jar", "source": {"sha1": "08ccc4548b3be68443025e760beef87cb5d229db", "sha256": "f283b548f63613e034a0841e2eaf347c45d8b1c59bddceac6dd27f346e62f1fd", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-protobuf_2.12/2.5.19/akka-protobuf_2.12-2.5.19-sources.jar"} , "name": "com_typesafe_akka_akka_protobuf_2_12", "actual": "@com_typesafe_akka_akka_protobuf_2_12//jar", "bind": "jar/com/typesafe/akka/akka_protobuf_2_12"},
    {"artifact": "com.typesafe.akka:akka-stream_2.12:2.5.19", "lang": "scala", "sha1": "e5bc9a2299748ea16e9f649ffc22aa6e86245dd1", "sha256": "eb254be1e088716139e05d9f6d46ab5e6c41ee0771b08789990267ef7b82b309", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-stream_2.12/2.5.19/akka-stream_2.12-2.5.19.jar", "source": {"sha1": "66e9363d9e15a614d05b91f2ce0a3b1891955528", "sha256": "4b4721ef336b06b05238ed13ef772bee2dbc06d3de2622cf5776b1d6382ec3cb", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/akka/akka-stream_2.12/2.5.19/akka-stream_2.12-2.5.19-sources.jar"} , "name": "com_typesafe_akka_akka_stream_2_12", "actual": "@com_typesafe_akka_akka_stream_2_12//jar:file", "bind": "jar/com/typesafe/akka/akka_stream_2_12"},
    {"artifact": "com.typesafe:config:1.3.3", "lang": "java", "sha1": "4b68c2d5d0403bb4015520fcfabc88d0cbc4d117", "sha256": "b5f1d6071f1548d05be82f59f9039c7d37a1787bd8e3c677e31ee275af4a4621", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/config/1.3.3/config-1.3.3.jar", "source": {"sha1": "c7af5bd41815a5edc8e7a81082e88fe18f9729e0", "sha256": "fcd7c3070417c776b313cc559665c035d74e3a2b40a89bbb0e9bff6e567c9cc8", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/config/1.3.3/config-1.3.3-sources.jar"} , "name": "com_typesafe_config", "actual": "@com_typesafe_config//jar", "bind": "jar/com/typesafe/config"},
    {"artifact": "com.typesafe:ssl-config-core_2.12:0.3.6", "lang": "java", "sha1": "09f1d652e6be462324e359eaa9a42ad4fbe0956c", "sha256": "ee78abc0773e1044a141631c08f9a8396a77034e69ad0524394ec99597cdc193", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/ssl-config-core_2.12/0.3.6/ssl-config-core_2.12-0.3.6.jar", "source": {"sha1": "2deb2e6cd0165bb94af04c84c716a4ee11a6f8eb", "sha256": "1357ac5d5bdbac879eaa8c29136d570789670634e6635634e84c51e32d71801f", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/com/typesafe/ssl-config-core_2.12/0.3.6/ssl-config-core_2.12-0.3.6-sources.jar"} , "name": "com_typesafe_ssl_config_core_2_12", "actual": "@com_typesafe_ssl_config_core_2_12//jar", "bind": "jar/com/typesafe/ssl_config_core_2_12"},
    {"artifact": "org.reactivestreams:reactive-streams:1.0.2", "lang": "java", "sha1": "323964c36556eb0e6209f65c1cef72b53b461ab8", "sha256": "cc09ab0b140e0d0496c2165d4b32ce24f4d6446c0a26c5dc77b06bdf99ee8fae", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/reactivestreams/reactive-streams/1.0.2/reactive-streams-1.0.2.jar", "source": {"sha1": "fb592a3d57b11e71eb7a6211dd12ba824c5dd037", "sha256": "963a6480f46a64013d0f144ba41c6c6e63c4d34b655761717a436492886f3667", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/reactivestreams/reactive-streams/1.0.2/reactive-streams-1.0.2-sources.jar"} , "name": "org_reactivestreams_reactive_streams", "actual": "@org_reactivestreams_reactive_streams//jar", "bind": "jar/org/reactivestreams/reactive_streams"},
    {"artifact": "org.scala-lang.modules:scala-java8-compat_2.12:0.8.0", "lang": "java", "sha1": "1e6f1e745bf6d3c34d1e2ab150653306069aaf34", "sha256": "d9d5dfd1bc49a8158e6e0a90b2ed08fa602984d815c00af16cec53557e83ef8e", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/modules/scala-java8-compat_2.12/0.8.0/scala-java8-compat_2.12-0.8.0.jar", "source": {"sha1": "0a33ce48278b9e3bbea8aed726e3c0abad3afadd", "sha256": "c0926003987a5c21108748fda401023485085eaa9fe90a41a40bcf67596fff34", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/modules/scala-java8-compat_2.12/0.8.0/scala-java8-compat_2.12-0.8.0-sources.jar"} , "name": "org_scala_lang_modules_scala_java8_compat_2_12", "actual": "@org_scala_lang_modules_scala_java8_compat_2_12//jar", "bind": "jar/org/scala_lang/modules/scala_java8_compat_2_12"},
    {"artifact": "org.scala-lang.modules:scala-parser-combinators_2.12:1.1.1", "lang": "java", "sha1": "29b4158f9ddcc22d1c81363fd61a8bef046f06b9", "sha256": "a8a2c6b31457c9ef15c63f5f1ac6b45e6b979059627e38c24416a900098ffb2e", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.12/1.1.1/scala-parser-combinators_2.12-1.1.1.jar", "source": {"sha1": "2e4d6675372d0ef2362de2906b292f9229ebc1a9", "sha256": "b005ff7606449700f8ec6d8b5866e1a7d62b80b00b80c005bb4d2677eb353624", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.12/1.1.1/scala-parser-combinators_2.12-1.1.1-sources.jar"} , "name": "org_scala_lang_modules_scala_parser_combinators_2_12", "actual": "@org_scala_lang_modules_scala_parser_combinators_2_12//jar", "bind": "jar/org/scala_lang/modules/scala_parser_combinators_2_12"},
# duplicates in org.scala-lang:scala-library promoted to 2.12.8
# - com.typesafe.akka:akka-actor_2.12:2.5.19 wanted version 2.12.8
# - com.typesafe.akka:akka-http-core_2.12:10.1.8 wanted version 2.12.8
# - com.typesafe.akka:akka-http_2.12:10.1.8 wanted version 2.12.8
# - com.typesafe.akka:akka-parsing_2.12:10.1.8 wanted version 2.12.8
# - com.typesafe.akka:akka-protobuf_2.12:2.5.19 wanted version 2.12.8
# - com.typesafe.akka:akka-stream_2.12:2.5.19 wanted version 2.12.8
# - com.typesafe:ssl-config-core_2.12:0.3.6 wanted version 2.12.6
# - org.scala-lang.modules:scala-java8-compat_2.12:0.8.0 wanted version 2.12.0
# - org.scala-lang.modules:scala-parser-combinators_2.12:1.1.1 wanted version 2.12.6
    {"artifact": "org.scala-lang:scala-library:2.12.8", "lang": "java", "sha1": "36b234834d8f842cdde963c8591efae6cf413e3f", "sha256": "321fb55685635c931eba4bc0d7668349da3f2c09aee2de93a70566066ff25c28", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/scala-library/2.12.8/scala-library-2.12.8.jar", "source": {"sha1": "45ccb865e040cbef5d5620571527831441392f24", "sha256": "11482bcb49b2e47baee89c3b1ae10c6a40b89e2fbb0da2a423e062f444e13492", "repository": "https://repo.maven.apache.org/maven2", "url": "https://repo.maven.apache.org/maven2/org/scala-lang/scala-library/2.12.8/scala-library-2.12.8-sources.jar"} , "name": "org_scala_lang_scala_library", "actual": "@org_scala_lang_scala_library//jar", "bind": "jar/org/scala_lang/scala_library"},
    ]

def maven_dependencies(callback = jar_artifact_callback):
    for hash in list_dependencies():
        callback(hash)
