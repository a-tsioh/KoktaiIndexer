package datatypes

// trait for companion objects
trait Indexable[T] {
  val indexName: String
  val docType: String
  def getSchema(): String
  def fromJson(str: String): Option[T]
}
