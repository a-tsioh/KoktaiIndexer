package datatypes

import ujson.Js

object ElasticMapping {

  def fromPropertiesTypes(doctype: String, props: Map[String,Js.Str]): Js.Obj = {
    val jsPayload: Js.Obj = Js.Obj.from(props.mapValues {t => Js.Obj("type" -> t)})
    Js.Obj(doctype ->  Js.Obj("properties" -> jsPayload))
  }

  val text = Js.Str("text")
  val keyword = Js.Str("keyword")



}
