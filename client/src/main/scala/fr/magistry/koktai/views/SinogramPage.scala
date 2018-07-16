package fr.magistry.koktai.views


import diode.Dispatcher
import scalatags.JsDom.all._

class SinogramPage(dispatch: Dispatcher, s: api.Sinogram) extends Page {


  protected val content =
    div(
      cls:="ui card",
      div(
        cls:="content",
        div(cls:="header",s.orth),
        div(cls:="meta",s.pron),
        div(cls:="meta",s.comment),
        div(cls:="content",
          div(cls:="ui list",
            div(cls:="item",
              div(cls:="header","國音"),
              s.guoyin
            ),
            div(cls:="item",
              div(cls:="header","台甘"),
              s.taikam
            ),
            div(cls:="item",
              div(cls:="header","普閩"),
              s.phooban
            )
          ),
          "詞",
          div(cls:="ui list",
            s.words.map(w => div(cls:="item",w))
            )
        )
      )
    ).render


  override def render() = content

  override def registerSemUICallbacks(): Unit = ()

}
