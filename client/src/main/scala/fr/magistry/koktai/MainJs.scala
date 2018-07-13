package fr.magistry.koktai

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import org.scalajs.dom.html

@JSExportTopLevel("MainJs")
object MainJs {

  @JSExport
  def main(args: Array[String]): Unit = {
    dom.window.alert("scalajs called")
  }


}
