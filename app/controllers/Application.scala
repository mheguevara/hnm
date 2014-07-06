package controllers

import play.api._
import play.api.mvc._
import registry.ProductionEnvironment
import components.hnservice.HNServiceComponent
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import components.archive.ArchiveComponent
import exceptions.{HNPageParsingFailureException, HNPageRetrievalFailureException}

trait Application extends Controller with HNServiceComponent with ArchiveComponent {

  private val logger = Logger(getClass)
  
  def hnPage = Action.async {

    hnService.getLatestPage map { hnPage =>

      archive.save(hnPage)

      Ok(views.html.hnPageView(Some(hnPage), None))

    } recoverWith {

      case e@(_: HNPageRetrievalFailureException | _: HNPageParsingFailureException) => {

        logger.error(e.getMessage, e)

        archive.getMostRecentHNPage map { maybeSnapshot =>

          Ok(views.html.hnPageView(maybeSnapshot, Some(e.getMessage)))

        }

      }

    }

  }
}

object Application extends Application with ProductionEnvironment
