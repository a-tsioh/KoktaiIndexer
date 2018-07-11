import java.net.InetAddress

import datatypes._
import org.apache.http.HttpHost
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.{Client, RestClient, RestHighLevelClient}
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.xcontent.{XContent, XContentType}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder

import scala.xml.{Elem, Node, XML}


class MyClient {

  val client: RestHighLevelClient =
    new RestHighLevelClient(
      RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http")
      )
    )

  def doSomething() = {

  }

  def buildIndexRequest[T](doc: Jsonable[T]): IndexRequest = {
    val r = new IndexRequest(
      doc.indexName,
      doc.docType)
    doc.getID.foreach(r.id)
    r.source(doc.toJson, XContentType.JSON)
    r
  }

  def bulkIndex(requests: Seq[IndexRequest]): Unit = {
    val bulkReq = new BulkRequest()
    requests.foreach(bulkReq.add)
    client.bulk(bulkReq)
  }


  private def batchProcessing[T](batchSize: Int, data: Seq[T], f: Seq[T] => Unit): Unit ={
    data match {
      case Nil => ()
      case _ =>
        val (hd, tl) = data.splitAt(batchSize)
        f(hd)
        batchProcessing(batchSize,tl,f)
    }
  }



  def createIndex[T](idx: Indexable[T]): Unit = {
    util.Try {client.indices.delete(new DeleteIndexRequest(idx.indexName))}
    val idxReq = new CreateIndexRequest(idx.indexName)
    idxReq.mapping(idx.docType, idx.getSchema(), XContentType.JSON)
    client.indices.create(idxReq)
  }

  def insertData[T](idx: KokTaiRecord[T], data: Seq[Node]): Unit = {
    val reqs = for(e <- data.par; cd <- idx.fromXML(e)) yield buildIndexRequest(cd)
    batchProcessing(1000, reqs.seq, bulkIndex)
  }


  def indexAllCharDecl(xmlRoot: Elem): Unit = {
    createIndex(CharDecl)
    val data = xmlRoot \\ "charDecl"
    insertData(CharDecl, data)
  }

  def indexAllChapters(xmlRoot: Elem): Unit = {
    createIndex(Chapter)
    val data = xmlRoot \\ "body" \ "div"
    insertData(Chapter, data)
  }

  def indexAllSinograms(xmlRoot: Elem): Unit = {
    createIndex(Sinogram)
    val data = xmlRoot \\ "entryFree"
    insertData(Sinogram, data)
  }
  def indexAllWords(xmlRoot: Elem): Unit = {
    createIndex(Word)
    val data = xmlRoot \\ "entry"
    insertData(Word, data)
  }



  def putMapping[T](idx: Indexable[T]): Unit = {
    val req = new PutMappingRequest(idx.indexName)
    req.`type`(idx.docType)
    req.source(idx.getSchema(), XContentType.JSON)
    client.indices().putMapping(req)
  }

  def test() = {
    val x = XML.loadFile("/tmp/k.xml")
    indexAllCharDecl(x)
    indexAllChapters(x)
    indexAllSinograms(x)
    indexAllWords(x)
  }

  def searchExample() = {
    val req = new SearchRequest(CharDecl.indexName)
    req.types(CharDecl.docType)
    val reqSource = new SearchSourceBuilder()
    reqSource.query(QueryBuilders.matchQuery("name", "TSU"))
    req.source(reqSource)
    val result = client.search(req)
    val hits = result.getHits.getHits
    for( hit <- hits) {
      val c = CharDecl.fromJson(hit.getSourceAsString)
      println(c)
    }
  }

}