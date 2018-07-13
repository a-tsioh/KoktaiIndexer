package fr.magistry.koktai.views

import diode.Dispatcher
import fr.magistry.koktai.Search
import org.scalajs.dom
import org.scalajs.dom.html

import scalatags.JsDom.all._

class SearchInput(dispatch: Dispatcher) {

  def render = {
    val i = input(placeholder := "search...", `type` := "text").render
    i.onchange = (_) => {
      dom.window.alert("coucou")
      dispatch(Search(i.value))
    }
    i
  }
}
