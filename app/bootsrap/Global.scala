package bootsrap

import play.api.{Logger, Application, GlobalSettings}

/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */
object Global extends GlobalSettings {

  private val logger = Logger(getClass)

  override def onStart(app: Application): Unit = {

    logger.info("HNM started!")

  }

  override def onStop(app: Application): Unit = {

    logger.info("HNM stopped!")

  }

}
