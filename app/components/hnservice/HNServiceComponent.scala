package components.hnservice

import scala.concurrent.Future
import java.util.Date
import models.HNPage

trait HNServiceComponent {

  val hnService: HNService

  trait HNService {

    def getLatestPage: Future[HNPage]

  }

}
