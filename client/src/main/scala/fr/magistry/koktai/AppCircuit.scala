package fr.magistry.koktai

import org.scalajs.dom
import diode._
import api.Elem
import fr.magistry.koktai.wiring.Client
import autowire._

import scalajs.concurrent.JSExecutionContext.Implicits.queue
import boopickle.Default._

import scala.util.{Failure, Success}

// data model
case class RootModel(
                      search:String,
                      results: Seq[Elem],
                      selected: Option[(Int,Elem)],
                    )

// actions

case class Search(str: String) extends Action
case class NewResults(li: Seq[Elem]) extends Action
case class Select(el: Elem) extends Action




object AppCircuit extends Circuit[RootModel]{
  override protected def initialModel: RootModel = RootModel("", Nil, None)

  protected var resultList: Option[views.ResultsList] = None
  def registerResultList(rl: views.ResultsList): Unit = resultList = Some(rl)

  protected var readerView: Option[views.ReaderView] = None
  def registerReaderView(rv: views.ReaderView): Unit = readerView = Some(rv)


  def performQuery(q: String) = Effect(
    Client[api.SharedApi].combinedSearch(q).call().map { wl =>
      NewResults(wl.map {
        case api.AnyElem(Some(w),None) => w
        case api.AnyElem(None, Some(s)) => s
      })
    }
  )

  val handleSearchEvents = new ActionHandler(zoomTo(_.search)) {
    override def handle = {
      case Search(s) =>
        updated(s, performQuery(s))
      case NewResults(rl) =>
        //rl.map({case w:api.Word => (w.orth, w.pron, w.pos)}).foreach(println)
        resultList.foreach { view =>
          view.clear()
          rl.foreach {
            case w:api.Word => view.appendWord(w)
            case s:api.Sinogram => view.appendSinogram(s)
          }
          view.show()
        }
        noChange
    }
  }

  val handleSelection = new ActionHandler(zoomTo(_.selected)) {
    override def handle = {
      case Select(w: api.Word) =>
        for(rv <- readerView) rv.showWord(w)
        noChange
      case Select(s: api.Sinogram) =>
        for(rv <- readerView) rv.showSinogram(s)
        noChange

    }
  }

  override protected def actionHandler = composeHandlers(
    handleSearchEvents,
    handleSelection
  )


}
