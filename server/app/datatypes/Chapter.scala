package datatypes
import scala.xml.Node

import upickle.default.{readJs, writeJs}

case class Chapter(
                    head: String,
                    words: Seq[String],
                    sinograms: Seq[String],
                    comment: String
                  ) extends Jsonable[Chapter]{
  override val indexName: String = Chapter.indexName
  override val docType: String = Chapter.docType

  override def getID: Option[String] = Some(head)

  override def toJson: String = writeJs[Chapter](this).toString
}


object Chapter extends Indexable[Chapter] with KokTaiRecord[Chapter] {
  import datatypes.{ElasticMapping => ES}
  override val indexName: String = "chapters"
  override val docType: String = "chapter"

  override def getSchema(): String =
    ES.fromPropertiesTypes(docType,Map(
      "head" -> ES.text,
      "words" -> ES.keyword,
      "sinograms" -> ES.keyword,
      "comment" -> ES.text
    )).toString()

  override def fromJson(str: String): Option[Chapter] = util.Try {
    val js = upickle.json.read(str)
    readJs[Chapter](js)
  }.toOption

  override def fromXML(xml: Node): Option[Chapter] = util.Try {
    val head: String = (xml \ "head").head.text
    val words: Seq[String] = ((xml \ "div").filter( _ \@ "type" == "words") \ "entry" \ "form" \ "orth" filter {_ \@ "type" == "full"} ).map( _.text)
    val sinograms: Seq[String] = ((xml \ "div").filter( _ \@ "type" == "words") \ "entryFree" \ "form" \ "orth").map(_.text)
    val comment: String =  (xml \ "p").text
    Chapter(head, words, sinograms, comment)
  }.toOption

}