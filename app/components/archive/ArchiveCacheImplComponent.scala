package components.archive

import scala.concurrent.Future
import models.HNPage
import play.api.cache.Cache
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

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
