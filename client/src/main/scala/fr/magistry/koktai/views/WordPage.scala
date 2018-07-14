package fr.magistry.koktai.views

import diode.Dispatcher
import scalatags.JsDom.all._

class WordPage(dispatch: Dispatcher, w: api.Word)  {

  protected val content =
    div(
      cls:="ui card",
      div(
        cls:="content",
        div(cls:="header",w.orth),
        div(cls:="meta",w.pron),
        div(cls:="meta",w.pos),
        div(cls:="description",w.definition)
      )
    ).render

  def render = content

}
