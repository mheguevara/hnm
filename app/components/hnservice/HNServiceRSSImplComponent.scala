package components.hnservice

import play.api.Logger
import scala.concurrent.Future
import models.{HNLink, HNPage}
import play.api.libs.ws.WS
import exceptions.{HNPageParsingFailureException, HNPageRetrievalFailureException}
import java.util.Date
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

trait HNServiceRSSImplComponent extends HNServiceComponent {

  override val hnService =new HNServiceRSSImpl

  class HNServiceRSSImpl extends HNService {

    private val logger = Logger(getClass)

    private val rssUrl = "https://news.ycombinator.com/rss"

    override def getLatestPage: Future[HNPage] = {

      logger.info(s"> getLatestPage")

      WS.url(rssUrl).get().map { response =>

        if(response.status == 200) {

          val items = response.xml \\ "item"

          logger.debug(s"GET $rssUrl returned 200 OK, parsing links from rss feed")

          var order = 1

          val links = items map { item =>

            logger.debug(s"order=$order")

            val title = (item \ "title").text
            val href = (item \ "link").text
            val comments = (item \ "comments").text

            logger.debug(s"title=$title")
            logger.debug(s"href=$href")
            logger.debug(s"comments=$comments")

            val hnLink = HNLink(order, href, title, comments)

            order = order + 1

            hnLink

          }

          if(links.size == 0) throw new HNPageParsingFailureException(s"parsing the body for links failed\nbody=${response.body}")

          val res = HNPage(links, new Date().toString)

          logger.info(s"< getLatestPage")

          res

        } else {

          val line = s"GET $rssUrl returned ${response.status}\n${response.body}"
          logger.error(line)
          throw new HNPageRetrievalFailureException(line)

        }

      }

    }
  }

}
