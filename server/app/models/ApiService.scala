package models

import xml.XML

object ApiService extends api.SharedApi {

  val esClient = new ElasticSerchClient()

  private def textOfXML(node: xml.Node): Seq[api.Text] ={
    node.child map {
      case x if x.label == "#PCDATA" => api.SimpleText(x.text)
      case x if x.label == "g" =>
        val ref = x.attribute("ref").get.text
        val img = esClient.getCharDecl(ref).get.img
        ref match {
          case r if r.startsWith("ruby") => api.Ruby(x.text, img)
          case r if r.startsWith("mapped") => api.KoktaiCJK(x.text, img)
          case r if r.startsWith("hj") => api.KoktaiIDS(x.text, img)
          case r if r.startsWith("missing") => api.KoktaiCJK("???", img)
          case r if r.startsWith("to-check") => api.KoktaiCJK(x.text,img)
        }
    }
  }

  def apiWordOfEsWord(w: datatypes.Word): api.Word = {
    val wordXML = XML.loadString(w.xmlSource)
    api.Word(
      textOfXML((wordXML \\ "orth").filter(_ \@ "type" == "full").head),
      textOfXML((wordXML \\ "orth").filter(_ \@ "type" == "no-zhuyin").head),
      w.pron,
      w.pos,
      textOfXML((wordXML \\ "def").head),
      w.xmlSource)
  }

  override def test(): api.Chapter = api.Chapter("coucou",Nil,Nil,"comment")

  override def simpleWordSearch(query: String): Seq[api.Word] =
    esClient.searchWords(query).map(apiWordOfEsWord)

  override def combinedSearch(query: String): Seq[api.AnyElem] = {
    esClient.searchCombined(query).map {
      case w: datatypes.Word =>
        val apiWord = apiWordOfEsWord(w)
        api.AnyElem(
          Some(apiWord), None)
      case datatypes.Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource) =>
        val x = XML.loadString(xmlSource)
        api.AnyElem(
          None,
          Some(
            api.Sinogram(
              textOfXML((x \\ "orth").head),
              pron,
              (x \\ "note").filter(_ \@ "type" == "comment").headOption.map(textOfXML).getOrElse(Nil),
              (x \\ "note").filter(_ \@ "type" == "國音").headOption.map(textOfXML).getOrElse(Nil),
              (x \\ "note").filter(_ \@ "type" == "台甘").headOption.map(textOfXML).getOrElse(Nil),
              (x \\ "note").filter(_ \@ "type" == "普閩").headOption.map(textOfXML).getOrElse(Nil),
              words,
              xmlSource)
          )
        )
    }
  }


}
