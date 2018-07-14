package fr.magistry.koktai.views

import diode.Dispatcher

import scalatags.JsDom.all._



class ReaderView(dispatch: Dispatcher)  {
  val $ = scala.scalajs.js.Dynamic.global.$

  protected val seg = div(
    cls:= "ui segment"
  ).render

  def render() = seg

  def showWord(w: api.Word): Unit = {
    while(seg.hasChildNodes()) seg.removeChild(seg.lastChild)
    val wp = new WordPage(dispatch, w)
    seg.appendChild(wp.render)
    $(".shape").shape("set next side", "#side_reader")
    $(".shape").shape("flip back")
  }

}
