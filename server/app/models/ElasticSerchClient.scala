package models

import java.net.InetAddress

import org.apache.http.HttpHost
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder
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
import datatypes.{Chapter, CharDecl, Indexable, Jsonable, KokTaiRecord, Sinogram, Word}
import org.elasticsearch.action.get.GetRequest


class ElasticSerchClient {

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
    batchProcessing(5000, reqs.seq, bulkIndex)
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

  def indexFile(path:String) = {
    val x = XML.loadFile(path)
    indexAllSinograms(x)
    indexAllCharDecl(x)
    indexAllChapters(x)
    indexAllWords(x)
  }


  def getCharDecl(id: String): Option[CharDecl] = {
    val req = new GetRequest(CharDecl.indexName, CharDecl.docType,id)
    val result = client.get(req)
    if(result.isExists) CharDecl.fromJson(result.getSourceAsString)
    else None
  }


  def searchWords(query: String): Seq[Word] = {
    val req = new SearchRequest(Word.indexName)
    req.types(Word.docType)
    val reqSource = new SearchSourceBuilder()
    reqSource.query(QueryBuilders.matchQuery("orth", query))
    req.source(reqSource)
    val result = client.search(req)
    (for(hit <- result.getHits.getHits) yield Word.fromJson(hit.getSourceAsString)).toList.flatten
  }

  def searchCombined(query: String) = {
    val req = new SearchRequest()
    val reqSource = new SearchSourceBuilder()
    reqSource.query(
      QueryBuilders.multiMatchQuery(query, "orth", "words", "definition", "defTailo", "tailo")
        .field("orth", 3.0f)
        .field("words", 2.0f)
        .field("tailo", 2.0f)
        .field("definition",1.0f)
        .field("defTailo", 1.0f)
        .`type`("phrase")

    )
    reqSource.size(30)
    req.source(reqSource)
    val result = client.search(req)
    (for(hit <- result.getHits.getHits) yield {
      hit.getType match {
        case "word" => Word.fromJson(hit.getSourceAsString)
        case "sinogram" => Sinogram.fromJson(hit.getSourceAsString)
        case _ => None
      }
    }).toList.flatten
    }


  def searchExample() = {
    val req = new SearchRequest(Word.indexName)
    req.types(Word.docType)
    val reqSource = new SearchSourceBuilder()
    val bq = QueryBuilders.boolQuery()
    bq.must(QueryBuilders.multiMatchQuery("好","orth", "definition"))
    reqSource.query(bq)
      //QueryBuilders.matchQuery("name", "TSU"))
    req.source(reqSource)
    val result = client.search(req)
    val hits = result.getHits.getHits
    for( hit <- hits) {
      val c = Word.fromJson(hit.getSourceAsString)
      println(c)
    }
  }

  def close(): Unit = client.close()

}
