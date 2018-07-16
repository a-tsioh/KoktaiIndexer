package models

object PopulateES {

  val cl = new ElasticSerchClient()

  def main(args: Array[String]): Unit = {
    val xmlFile = args(0)
    cl.indexFile(xmlFile)
    cl.close()
  }



}
