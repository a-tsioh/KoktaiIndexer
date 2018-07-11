package datatypes
import scala.xml.Node

import upickle.default.{readJs, writeJs}

case class Sinogram(
                     orth: String,
                     pron: String,
                     comment: String,
                     guoyin: String,
                     taikam: String,
                     phooban: String,
                     words: Seq[String],
                     xmlSource: String
                   ) extends Jsonable[Sinogram] {
  override val indexName: String = Sinogram.indexName
  override val docType: String = Sinogram.docType

  override def getID: Option[String] = None

  override def toJson: String = writeJs[Sinogram](this).toString()
}

object Sinogram extends Indexable[Sinogram] with KokTaiRecord[Sinogram] {
  import datatypes.{ElasticMapping => ES}

  override val indexName: String = "sinograms"
  override val docType: String = "sinogram"

  override def getSchema(): String = ES.fromPropertiesTypes(
    docType,
    Map(
      "orth" -> ES.keyword,
      "pron" -> ES.keyword,
      "comment" -> ES.text,
      "guoyin" -> ES.text,
      "taikam" -> ES.text,
      "phooban" -> ES.text,
      "words" -> ES.keyword,
      "xmlSource" -> ES.text
    )
  ).toString()

  override def fromJson(str: String): Option[Sinogram] = util.Try {
    val js = upickle.json.read(str)
    readJs[Sinogram](js)
  }.toOption

  override def fromXML(xml: Node): Option[Jsonable[Sinogram]] = util.Try {
    val orth = (xml \ "form" \ "orth").text
    val pron = (xml \ "form" \ "pron").text
    val comment = (xml \ "form" \ "note").filter( _ \@ "type" == "comment").text
    val guoyin = (xml \ "form" \ "note").filter( _ \@ "type" == "國音").text
    val taikam = (xml \ "form" \ "note").filter( _ \@ "type" == "comment").text
    val phooban = (xml \ "form" \ "note").filter( _ \@ "type" == "comment").text
    val words = (xml \ "superEntry" \ "entry" \ "form" \ "orth").filter(_ \@ "type" == "no-zhuyin").map(_.text)
    val xmlSource = xml.toString()
    Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource)
  }.toOption
}
