package fr.magistry.koktai.views

import diode.Dispatcher
import scalatags.JsDom.all._

class WordPage(dispatch: Dispatcher, w: api.Word) extends Page {
  val $ = scala.scalajs.js.Dynamic.global.$

  lazy val definition = {
    div(w.definition map {
      case api.SimpleText(t) => raw(t)
      case api.Ruby(r, img) =>
        val syl = utils.TsuIm.splitTone(r)
        div(
          cls := "ruby_chr",
          data("html"):= "<div class='content'><img src=\"/assets/" + img + "\"></img></div>",
          div(cls := "ruby_syl", syl.seg),
          div(cls := "ruby_tone", syl.tone)
        )
      case api.KoktaiCJK(cjk, img) => raw(cjk)
    })

  }

  protected val content =
    div(
      cls:="ui card",
      div(
        cls:="content",
        div(cls:="header",w.orth),
        div(cls:="meta",w.pron),
        div(cls:="meta",w.pos),
        div(cls:="description", definition)
      )
    ).render

  override def render = content

  override def registerSemUICallbacks():Unit = $(".ruby_chr").popup()

}
