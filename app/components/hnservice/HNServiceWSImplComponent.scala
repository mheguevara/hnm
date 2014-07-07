package components.hnservice

import scala.concurrent.Future
import models.{HNLink, HNPage}
import play.api.libs.ws.WS
import java.util.Date
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import exceptions.{HNPageParsingFailureException, HNPageRetrievalFailureException}

trait HNServiceWSImplComponent extends HNServiceComponent {

  override val hnService  = new HNServiceWSImpl

  class HNServiceWSImpl extends HNService {

    private val logger = Logger(getClass)

    private val titleR = """<td class="title"><a href="(.*?)".*?>(.*?)</a>(<span.*?>(.*?)</span>)?</td>""".r

    private val baseHNUrl = "https://news.ycombinator.com/"

    override def getLatestPage: Future[HNPage] = WS.url(baseHNUrl).get map { response =>

      logger.info(s"> getLatestPage")

      if(response.status == 200) {

        logger.debug("GET https://news.ycombinator.com/ returned 200 OK, parsing links")

        var order = 1

        val hnLinks: List[HNLink] = titleR.findAllMatchIn(response.body).map { m =>

          logger.debug(s"order=$order")

          val wholeMatch = m.group(0)
          logger.debug(s"wholeMatch=$wholeMatch")

          val href = m.group(1)
          logger.debug(s"href=$href")

          val finalHref =
            if(href.startsWith("/"))
              s"$baseHNUrl$href"
            else if(!href.startsWith("/") && !href.startsWith("http"))
              s"$baseHNUrl/$href"
            else
              href
          logger.debug(s"finalHref=$finalHref")

          val title = m.group(2)
          val site = m.group(4)
          logger.debug(s"title=$title")
          logger.debug(s"site=$site")

          val hnLink = HNLink(order, finalHref, title, site)
          order = order + 1
          hnLink
        }.filterNot(_.title == "More").toList

        if(hnLinks.size == 0) throw new HNPageParsingFailureException(s"parsing the body for links failed\nregex=$titleR\nbody=${response.body}")

        val res = HNPage(hnLinks, new Date().toString)

        logger.info(s"< getLatestPage")

        res

      } else {
        val line = s"GET $baseHNUrl returned ${response.status}\n${response.body}"
        logger.error(line)
        throw new HNPageRetrievalFailureException(line)
      }

    }

  }

}
