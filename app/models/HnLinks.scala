package models

import play.api.libs.json.Json

case class HnLink(index: Int, href: String, title: String, caption: String)

object HnLink {
  implicit val hnLinkJsonFormats = Json.format[HnLink]
}
