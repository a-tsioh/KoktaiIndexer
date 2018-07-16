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
    val wp = new WordPage(dispatch, w)
    show(wp)
  }

  def showSinogram(s: api.Sinogram): Unit = {
    val sp = new SinogramPage(dispatch, s)
    show(sp)
  }

  def show(p: Page): Unit = {
    while(seg.hasChildNodes()) seg.removeChild(seg.lastChild)
    seg.appendChild(p.render)
    $(".shape").shape("set next side", "#side_reader")
    $(".shape").shape("flip back")
    p.registerSemUICallbacks()
  }

}
