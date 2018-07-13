package fr.magistry.koktai



import scala.util.{Failure, Random, Success}
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.queue

import api.SharedApi
import wiring.Client

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import org.scalajs.dom.html
import autowire._
import boopickle.Default._

@JSExportTopLevel("MainJs")
object MainJs {

  val searchInput = new views.SearchInput(AppCircuit)

  @JSExport
  def main(args: Array[String]): Unit = {
    dom.document.getElementById("search_input").appendChild(searchInput.render)

    val ch = Client[SharedApi].test().call().onComplete {
      case Success(ch) => println(ch.toString)
      case Failure(f) => println(f.toString)
    }


  }


}
