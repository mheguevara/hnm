package components.archive

import scala.concurrent.Future
import models.HNPage
import play.api.cache.Cache
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext


/**
 * Copyright 2003-2014 Monitise Group Limited. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Monitise Group Limited.
 * Any reproduction of this material must contain this notice.
 */
trait ArchiveCacheImplComponent extends ArchiveComponent {

  override val archive = new ArchiveCacheImpl

  class ArchiveCacheImpl extends Archive {

    private val cacheKey = "archive.most.recent.hnpage"

    override def save(hnPage: HNPage): Future[Unit] = Future {
      Cache.set(cacheKey, hnPage)
    }

    override def getMostRecentHNPage: Future[Option[HNPage]] = Future {
      Cache.getAs[HNPage](cacheKey)
    }

  }

}
