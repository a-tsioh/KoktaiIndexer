package fr.magistry.koktai.views

import diode.Dispatcher
import fr.magistry.koktai.{AppCircuit, views}
import org.scalajs.dom
import org.scalajs.dom.raw.Element

import scalatags.JsDom.all._

class ResultsList(parentId: String, dispatch: Dispatcher) {
  val $ = scala.scalajs.js.Dynamic.global.$

  protected val mainDiv = div(cls:="ui middle aligned divided selection list").render

  def clear(): Unit = {
    if(mainDiv.hasChildNodes()) $(parentId).transition("slide up out")
    while (mainDiv.hasChildNodes()) {
      mainDiv.removeChild(mainDiv.lastChild)
    }
  }

  def show(): Unit = $(parentId).transition("slide down in")

  def appendWord(w:api.Word): Unit = {
    println(w.form)
    val item = new views.WordCard(dispatch, w).render
    mainDiv.appendChild(item)
  }

  def appendSinogram(s:api.Sinogram): Unit = {
    val item = new views.SinogramCard(dispatch, s).render
    mainDiv.appendChild(item)
  }

  def render = mainDiv

  def registerSemUICallbacks():Unit = $(parentId).transition()




}
