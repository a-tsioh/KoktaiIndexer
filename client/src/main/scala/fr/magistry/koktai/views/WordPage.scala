package fr.magistry.koktai.views

import diode.Dispatcher
import scalatags.JsDom.all._

class WordPage(dispatch: Dispatcher, w: api.Word) extends Page {
  val $ = scala.scalajs.js.Dynamic.global.$

  protected val content =
    div(
      cls:="ui card",
      div(
        cls:="content",
        div(cls:="header",TextRenderer.divOfText(w.orth)),
        div(cls:="meta",w.pron),
        div(cls:="meta",w.pos),
        div(cls:="description", TextRenderer.divOfText(w.definition))
      )
    ).render

  override def render = content

  override def registerSemUICallbacks():Unit = $(".with_popup").popup()


}
