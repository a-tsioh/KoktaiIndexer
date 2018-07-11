package datatypes

import upickle.default.{readJs, writeJs}

import scala.xml.Node

case class CharDecl(
                     id: String,
                     name: String,
                     img: String,
                     mappingStd: Option[String],
                     mappingPUA: Option[String],
                     xmlSource: String,
                   ) extends Jsonable[CharDecl]  {

  override def getID: Option[String] = Some(id)

  override def toJson: String = writeJs[CharDecl](this).toString()
  val indexName = CharDecl.indexName
  val docType = CharDecl.docType
}

object CharDecl extends Indexable[CharDecl] with KokTaiRecord[CharDecl] {
  val indexName = "chars"
  val docType = "charDecl"

  override def fromJson(str: String): Option[CharDecl] = {
    util.Try {
      val js = upickle.json.read(str)
      readJs[CharDecl](js)
    }
      .toOption
  }

  override def getSchema(): String = {
    import datatypes.{ElasticMapping => ES}
    ES.fromPropertiesTypes(docType,
      Map(
        "id" -> ES.keyword,
        "name" -> ES.text,
        "img" -> ES.keyword,
        "mappingStd" -> ES.keyword,
        "mappingPUA" -> ES.keyword,
        "xmlSource" -> ES.text
      )
    ).toString()
  }

  override def fromXML(xml: Node): Option[CharDecl] = {
    util.Try {
      val id: String = (xml \\ "char").head.attribute("http://www.w3.org/XML/1998/namespace", "id").head.text
      val name: String = (xml \\ "charName").head.text
      val img: String = (xml \ "char" \ "figure" \ "graphic").map( _ \@ "url").headOption.getOrElse("") // todo: tous devraient l'avoir
      val mappingStd: Option[String] = (xml \ "char" \ "mapping").filter( _ \@ "type" == "standard").map(_.text).headOption
      val mappingPUA: Option[String] = (xml \ "char" \ "mapping").filter( _ \@ "type" == "PUA").map(_.text).headOption
      val xmlSource = xml.toString
      CharDecl(id, name, img, mappingStd, mappingPUA, xmlSource)
    }   .toOption
  }



}