package experiments

import java.io.FileWriter

import xml.{Node, XML}
import datatypes.Word
import org.apache.http.HttpHost
import org.elasticsearch.action.search.{SearchRequest, SearchResponse, SearchScrollRequest}
import org.elasticsearch.client.{RestClient, RestHighLevelClient}
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder

object TsuImAlign {

  val client: RestHighLevelClient =
    new RestHighLevelClient(
      RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")
      )
    )

  def checkRuby(n: Node): Boolean = (n \@ "ref").startsWith("ruby")



  def getAllXMLs() = {
    val req = new SearchRequest(Word.indexName)
    val reqSrc = new SearchSourceBuilder()
    reqSrc.size(500)
    reqSrc.query(QueryBuilders.matchAllQuery())
    req.source(reqSrc)
    req.scroll(TimeValue.timeValueSeconds(60L))
    val fw = new FileWriter("/tmp/corpus.align")
    val result = client.search(req)
    var hits = result.getHits
    var scrollId = result.getScrollId
    while(hits.getHits.length > 0) {
      for (h <- hits.getHits; w <- Word.fromJson(h.getSourceAsString)) {
        val x = XML.loadString(w.xmlSource)
        val tsuim = (x \ "def" \ "g").filter(checkRuby).map(_.text) mkString " "
        val hj = (x \ "def").map(_.child.filterNot(checkRuby)).map(_.text).mkString("").split("") mkString " "
        if (hj.nonEmpty && tsuim.nonEmpty) fw.write(s"$tsuim ||| $hj\n")
      }
      val scrollReq = new SearchScrollRequest(scrollId)
      scrollReq.scroll(TimeValue.timeValueSeconds(60))
      val resultScroll = client.searchScroll(scrollReq)
      hits = resultScroll.getHits
      scrollId = resultScroll.getScrollId
    }
    fw.close()
  }


  def main(args: Array[String]): Unit = {
    getAllXMLs()
    client.close()
  }

}