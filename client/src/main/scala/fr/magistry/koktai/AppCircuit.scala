package fr.magistry.koktai

import org.scalajs.dom

import diode._

import elements.Elem


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

  val handleSearch = new ActionHandler(zoomTo(_.search)) {
    override def handle = {
      case Search(s) =>
        dom.window.alert(s) ;
        noChange
    }
  }

  override protected def actionHandler = composeHandlers(
    handleSearch
  )


}
