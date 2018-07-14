package fr.magistry.koktai.views

import diode.Dispatcher
import fr.magistry.koktai.Select

import scalatags.JsDom.all._

class SinogramCard(dispatch: Dispatcher, sinogram: api.Sinogram) {


  protected val b = div(
    cls:="ui icon button",
    i(cls:="angle right icon")
  ).render

  protected val d = div(
    cls:="ui item",
    div(cls:="ui left floated content", div(cls:="ui blue circular small label","字")),
    div(cls:="right floated large content", b),
    div(
      cls:="content",
      h2(cls:="ui header", s"${sinogram.orth}"),
      s"${sinogram.pron}")
  ).render


  b.onclick = (_) => {
    dispatch(Select(sinogram))
  }


  def render() = d


}
