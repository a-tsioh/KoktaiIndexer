package models

object ApiService extends api.SharedApi {

  val esClient = new ElasticSerchClient()


  override def test(): api.Chapter = api.Chapter("coucou",Nil,Nil,"comment")

  override def simpleWordSearch(query: String): Seq[api.Word] = {
    esClient.searchWords(query).map {w => api.Word(w.form,w.orth, w.pron, w.pos, w.definition, w.xmlSource)}
  }

  override def combinedSearch(query: String): Seq[api.AnyElem] = {
    esClient.searchCombined(query).map {
      case w: datatypes.Word => api.AnyElem(Some(api.Word(w.form,w.orth, w.pron, w.pos, w.definition, w.xmlSource)),None)
      case datatypes.Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource) =>
        api.AnyElem(None, Some(api.Sinogram(orth, pron, comment, guoyin, taikam, phooban, words, xmlSource)))
    }
  }


}
