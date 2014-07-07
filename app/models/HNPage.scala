package models


case class HNLink(order: Int, href: String, title: String, comments: String)

case class HNPage(links: Seq[HNLink], retrievedAt: String)