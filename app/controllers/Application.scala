package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS
import play.api.libs.json.{Json, JsValue}
import play.api.Play.current
import models.HnLink


object Application extends Controller {

  private val titleR = """<td class="title"><a href="(.*?)".*?>(.*?)</a><span.*?>(.*?)</span></td>""".r

  def index = Action {
    Ok(views.html.index())
  }
  
  def links = Action.async {
    WS.url("https://news.ycombinator.com/").get map { response =>
      var index = 1
      val hnLinks: List[HnLink] = titleR.findAllMatchIn(response.body).map { m =>
        val href = m.group(1)
        val title = m.group(2)
        val caption = m.group(3)
        val hnLink = HnLink(index, href, title, caption)
        index = index + 1
        hnLink
      }.toList
      Ok(views.html.links(hnLinks))
    }
  }

}
