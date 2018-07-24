package datatypes
import scala.xml.Node

import upickle.default.{readJs, writeJs}



case class Word(
                 form: String,
                 orth: String,
                 pron: String,
                 tailo: String,
                 pos: String,
                 definition: String,
                 defTsuIm: String,
                 defNoTsuIm: String,
                 defTailo: String,
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
      "tailo" -> ES.keyword,
      "pos" -> ES.keyword,
      "definition" -> ES.text,
      "defTsuIm" -> ES.text,
      "defNoTsuIm" -> ES.text,
      "defTailo" -> ES.text,
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
    val tailo= datatypes.convertTsuIm(pron)
    val pos = (xml \ "gramGrp" \ "pos").text
    val definition = (xml \ "def").text
    val defTsuIm = (xml \ "def" \ "g").filter(isRuby).map(_.text.replace(" ","")).mkString(" ")
    val defNoTsuIm = (xml \ "def").headOption.map(_.child.filter(!isRuby(_)).text).getOrElse("")
    val defTailo = datatypes.convertTsuIm(defTsuIm)
    val xmlSource = xml.toString()
    Word(form,orth, pron, tailo, pos, definition, defTsuIm, defNoTsuIm, defTailo, xmlSource)
  }.toOption
}
