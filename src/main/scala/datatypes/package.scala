import scala.xml.Node



package object datatypes {
  trait KokTaiRecord[T] {
    def fromXML(xml: Node): Option[Jsonable[T]]
  }


}
