package models

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger


object PopulateES {





  def main(args: Array[String]): Unit = {

    val root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
    root.setLevel(Level.ERROR)

    val cl = new ElasticSerchClient()
    val xmlFile = args(0)
    cl.indexFile(xmlFile)
    cl.close()
  }



}
