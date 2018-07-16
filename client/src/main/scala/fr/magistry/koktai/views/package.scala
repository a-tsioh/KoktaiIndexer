package fr.magistry.koktai

import org.scalajs.dom.html.Div
import scalatags.JsDom.all._

package object views {

  abstract class Page {
    def render : Div
    def registerSemUICallbacks(): Unit
  }

}
