package components.hnservice

import scala.concurrent.Future
import models.{HNLink, HNPage}
import play.api.libs.ws.WS
import java.util.Date
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import exceptions.{HNPageParsingFailureException, HNPageRetrievalFailureException}

/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */
trait HNServiceWSImplComponent extends HNServiceComponent {

  override val hnService  = new HNServiceWSImpl

  class HNServiceWSImpl extends HNService {

    private val logger = Logger(getClass)

    private val titleR = """<td class="title"><a href="(.*?)".*?>(.*?)</a>(<span.*?>(.*?)</span>)?</td>""".r

    override def getLatestPage: Future[HNPage] = WS.url("https://news.ycombinator.com/").get map { response =>

      if(response.status == 200) {

        logger.info("GET https://news.ycombinator.com/ returned 200 OK, parsing links")

        var order = 1

        val hnLinks: List[HNLink] = titleR.findAllMatchIn(response.body).map { m =>
          val href = m.group(1)
          val title = m.group(2)
          val site = m.group(4)
          val hnLink = HNLink(order, href, title, site)
          order = order + 1
          hnLink
        }.filterNot(_.title == "More").toList

        if(hnLinks.size == 0) throw new HNPageParsingFailureException(s"parsing the body for links failed")

        HNPage(hnLinks, new Date().toString)

      } else {
        val line = s"GET https://news.ycombinator.com/ returned ${response.status}\n${response.body}"
        logger.error(line)
        throw new HNPageRetrievalFailureException(line)
      }

    }

  }

}
