package models

/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */

case class HNLink(order: Int, href: String, title: String, comments: String)

case class HNPage(links: Seq[HNLink], retrievedAt: String)