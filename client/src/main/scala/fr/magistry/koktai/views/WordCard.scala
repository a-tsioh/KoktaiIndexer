package fr.magistry.koktai.views

import diode.Dispatcher
import fr.magistry.koktai.Select

import scalatags.JsDom.all._

class WordCard(dispatch: Dispatcher, word: api.Word) {


  protected val b = div(
    cls:="ui icon button",
    i(cls:="angle right icon")
  ).render

  protected val d = div(
    cls:="ui item",
    div(cls:="ui left floated content", div(cls:="ui green circular small label","詞")),
    div(cls:="right floated large content", b),
    div(
      cls:="content",
      h2(cls:="ui header", TextRenderer.divOfText(word.orth)),
      s"[${word.pos}] ${word.pron}")
  ).render


  b.onclick = (_) => {
    dispatch(Select(word))
  }


  def render() = d

}
