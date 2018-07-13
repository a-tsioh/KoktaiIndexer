package datatypes

import upickle.default.{readJs, writeJs}


// trait for case classes
trait Jsonable[T] {
  val indexName: String
  val docType: String
  def getID: Option[String]

  def toJson: String
}
