package models

import xml.XML

object ApiService extends api.SharedApi {

  val esClient = new ElasticSerchClient()

  private def renderDefinition(w: datatypes.Word) = {
    val x = XML.loadString(w.xmlSource)
    val definition = (x \\ "def").head.child map {
      case x if x.label == "#PCDATA" => api.SimpleText(x.text)
      case x if x.label == "g" =>
        if(x.attribute("ref").map(_.text.startsWith("ruby")).getOrElse(false)) {
          esClient.getCharDecl(x.attribute("ref").get.text).map { cdl =>
            api.Ruby(x.text,cdl.img)
          }
            .getOrElse(api.Ruby(x.text,""))
        }
        else api.KoktaiCJK(x.text,"Y")
    }
    definition
  }


  override def test(): api.Chapter = api.Chapter("coucou",Nil,Nil,"comment")

  override def simpleWordSearch(query: String): Seq[api.Word] = {
    esClient.searchWords(query).map {w => api.Word(w.form,w.orth, w.pron, w.pos, renderDefinition(w), w.xmlSource)}
  }

  override def combinedSearch(query: String): Seq[api.AnyElem] = {
    esClient.searchCombined(query).map {
      case w: datatypes.Word =>
        api.AnyElem(Some(api.Word(w.form,w.orth, w.pron, w.pos, renderDefinition(w), w.xmlSource)),None)
      case datatypes.Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource) =>
        api.AnyElem(None, Some(api.Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource)))
    }
  }


}
