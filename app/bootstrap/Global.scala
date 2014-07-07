package bootstrap

import play.api.{Logger, Application, GlobalSettings}
import play.api.mvc._
import scala.concurrent.Future
import java.util.UUID
import play.filters.gzip.GzipFilter
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object AccessLoggingFilter extends Filter {

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {

    val result = f(rh)

    val arrival = System.currentTimeMillis()

    result map { resp =>

      val duration = System.currentTimeMillis() - arrival

      val uuid = UUID.randomUUID().toString

      if(Logger.isDebugEnabled) {

        val reqLog = s"\n> ${rh.method} ${rh.uri}${rh.headers.toSimpleMap.map(l => s"> ${l._1}: ${l._2}").mkString("\n", "\n", "\n")}"
        val resLog = s"\n< ${resp.header.status}${resp.header.headers.map(l => s"< ${l._1}: ${l._2}").mkString("\n", "\n", "\n")}"

        Logger.debug(s"$uuid - $reqLog$resLog")

      }

      val line = s"$uuid - ${rh.method} ${rh.uri} ${resp.header.status} ${duration}ms${rh.session.get("username").fold("")(s => s" $s")}"

      if(rh.uri.contains("assets"))
        Logger.debug(line)
      else
        Logger.info(line)

      resp

    }

  }

}

object Global extends WithFilters(AccessLoggingFilter, new GzipFilter) {

  override def onStart(app: Application): Unit = {

    Logger.info("HNM started!")

  }

  override def onStop(app: Application): Unit = {

    Logger.info("HNM stopped!")

  }

}
