package monarchy.util.http

import java.net.URLEncoder

case class UrlBuilder(
    baseUrl: String,
    query: Map[String, String] = Map.empty
) {
  import UrlBuilder._

  def query(key: String, value: String): UrlBuilder =
    query(key -> value)

  def query(params: (String, String)*): UrlBuilder =
    copy(query = query ++ params)

  def build: String = {
    val queryString = query.map(encodeParam).mkString("&")
    if (queryString.isEmpty) baseUrl else s"$baseUrl?$queryString"
  }
}

object UrlBuilder {
  private def encodeParam(param: (String, String)): String =
    s"${encode(param._1)}=${encode(param._2)}"

  private def encode(component: String): String =
    URLEncoder.encode(component, "UTF-8")
}
