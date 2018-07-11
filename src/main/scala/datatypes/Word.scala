package datatypes
import scala.xml.Node

import upickle.default.{readJs, writeJs}


case class Word(
                 form: String,
                 orth: String,
                 pron: String,
                 pos: String,
                 definition: String,
                 xmlSource: String
               ) extends Jsonable[Word]{
  override val indexName: String = Word.indexName
  override val docType: String = Word.docType

  override def getID: Option[String] = None

  override def toJson: String = writeJs[Word](this).toString
}

object Word extends Indexable[Word] with KokTaiRecord[Word] {
  import datatypes.{ElasticMapping => ES}
  override val indexName: String = "words"
  override val docType: String = "word"

  override def getSchema(): String = ElasticMapping.fromPropertiesTypes(
    docType,
    Map(
      "form" -> ES.keyword,
      "orth" -> ES.keyword,
      "pron" -> ES.keyword,
      "pos" -> ES.keyword,
      "definition" -> ES.text,
      "xmlSource" -> ES.text
    )
  ).toString()

  override def fromJson(str: String): Option[Word] = util.Try {
    val js = upickle.json.read(str)
    readJs[Word](js)
  }.toOption

  override def fromXML(xml: Node): Option[Jsonable[Word]] = util.Try {
    val form = (xml \ "form" \ "orth").filter(_ \@ "type" == "full").text
    val orth = (xml \ "form" \ "orth").filter(_ \@ "type" == "no-zhuyin").text
    val pron = (xml \ "form" \ "pron").text
    val pos = (xml \ "gramGrp" \ "pos").text
    val definition = (xml \ "def").text
    val xmlSource = xml.toString()
    Word(form,orth, pron, pos, definition, xmlSource)
  }.toOption
}
