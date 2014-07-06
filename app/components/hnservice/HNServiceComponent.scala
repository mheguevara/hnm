package components.hnservice

import scala.concurrent.Future
import java.util.Date
import models.HNPage


/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */
trait HNServiceComponent {

  val hnService: HNService

  trait HNService {

    def getLatestPage: Future[HNPage]

  }

}
