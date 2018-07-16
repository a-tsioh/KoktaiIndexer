package fr.magistry.koktai.views

import diode.Dispatcher
import scalatags.JsDom.all._

class WordPage(dispatch: Dispatcher, w: api.Word)  {

  lazy val definition = {
    div(w.definition map {
      case api.SimpleText(t) => raw(t)
      case api.Ruby(r,t,img) => div(
        cls:="ruby_chr",
        div(cls:="ruby_syl", r)
      )
      case api.KoktaiCJK(cjk,img) => raw(cjk)
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

  def render = content

}
