import components.archive.ArchiveComponent
import components.hnservice.HNServiceComponent
import controllers.Application
import exceptions.{HNPageParsingFailureException, HNPageRetrievalFailureException}
import models.{HNLink, HNPage}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with Mockito {

  trait MockEnvironment extends HNServiceComponent with ArchiveComponent {
    override val hnService = mock[HNService]
    override val archive = mock[Archive]
  }

  val application = new Application with MockEnvironment

  "Application" should {

    "should render the latest page" in new WithApplication {

      application.hnService.getLatestPage returns Future.successful(HNPage(Seq(HNLink(1, "href", "title", "site")), "now"))

      val hnPage = application.hnPage.apply(FakeRequest())

      status(hnPage) must equalTo(OK)
      contentAsString(hnPage) must contain ("""<a href="href" class="links-link">title</a>""")

    }

    "should render a page from archive and display an error message" in new WithApplication {

      val error = "cant get hn page"

      application.hnService.getLatestPage returns Future.failed(new HNPageRetrievalFailureException(error))
      application.archive.getMostRecentHNPage returns Future.successful(Some(HNPage(Seq(HNLink(1, "href", "title", "site")), "now")))

      val hnPage = application.hnPage.apply(FakeRequest())

      status(hnPage) must equalTo(OK)
      contentAsString(hnPage) must contain ("""<a href="href" class="links-link">title</a>""")
      contentAsString(hnPage) must contain ("""<p>An error occurred during retrieval. Page you see may be outdated.</p>""")
      contentAsString(hnPage) must contain (s"""<p>$error</p>""")

    }

    "should render a page from archive and display an error message with failed parsing" in new WithApplication {

      val error = "cant parse hn page"

      application.hnService.getLatestPage returns Future.failed(new HNPageParsingFailureException(error))
      application.archive.getMostRecentHNPage returns Future.successful(Some(HNPage(Seq(HNLink(1, "href", "title", "site")), "now")))

      val hnPage = application.hnPage.apply(FakeRequest())

      status(hnPage) must equalTo(OK)
      contentAsString(hnPage) must contain ("""<a href="href" class="links-link">title</a>""")
      contentAsString(hnPage) must contain ("""<p>An error occurred during retrieval. Page you see may be outdated.</p>""")
      contentAsString(hnPage) must contain (s"""<p>$error</p>""")

    }

  }
}
