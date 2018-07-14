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

import scala.scalajs.js.Dynamic.global

@JSExportTopLevel("MainJs")
object MainJs {
  val $ = global.$

  @JSExport
  def main(args: Array[String]): Unit = {
    val searchInput = new views.SearchInput(AppCircuit)
    dom.document.getElementById("search_input").appendChild(searchInput.render)

    val sidebar = dom.document.getElementById("sidebar")
    val resultsView = new views.ResultsList("#sidebar", AppCircuit)
    sidebar.appendChild(resultsView.render)


    val main = dom.document.getElementById("main_zone")
    val readerView = new views.ReaderView(AppCircuit)
    main.appendChild(readerView.render)


    AppCircuit.registerResultList(resultsView)
    AppCircuit.registerReaderView(readerView)

    $(".shape").shape()

    $("#button_about").click( () => {
      $(".shape").shape("set next side", "#side_about")
      $(".shape").shape("flip back")
    })


  }


}
