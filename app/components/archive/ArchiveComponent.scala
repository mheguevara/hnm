package components.archive

import models.HNPage
import scala.concurrent.Future

trait ArchiveComponent {

  val archive: Archive

  trait Archive {

    def save(hnPage: HNPage): Future[Unit]

    def getMostRecentHNPage: Future[Option[HNPage]]

  }


}
