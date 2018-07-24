import scala.xml.Node



package object datatypes {
  trait KokTaiRecord[T] {
    def fromXML(xml: Node): Option[Jsonable[T]]
  }

  def isRuby(n: Node): Boolean = (n \@ "ref").startsWith("ruby")

  def convertTsuIm(tsuim: String): String = {
    tsuim.map {
      case ' ' => " "
      case 'ㄅ' => "p"
      case 'ㄆ' => "ph"
      case 'ㄇ' => "m"
      case 'ㄈ' => "f"
      case 'ㄉ' => "t"
      case 'ㄊ' => "th"
      case 'ㄋ' => "n"
      case 'ㄌ' => "l"
      case 'ㄍ' => "k"
      case 'ㄎ' => "kh"
      case 'ㄏ' => "h"
      case 'ㄐ' => "ts"
      case 'ㄑ' => "tsh"
      case 'ㄒ' => "s"
      case 'ㄖ' => "l"
      case 'ㄗ' => "ts"
      case 'ㄘ' => "tsh"
      case 'ㄙ' => "s"
      case 'ㄚ' => "a"
      case 'ㄛ' => "oo"
      case 'ㄜ' => "o"
      case 'ㄝ' => "e"
      case 'ㄞ' => "ai"
      case 'ㄟ' => "ei"
      case 'ㄠ' => "au"
      case 'ㄡ' => "ou"
      case 'ㄢ' => "an"
      case 'ㄣ' => "n"
      case 'ㄤ' => "ang"
      case 'ㄥ' => "ng"
      case 'ㄦ' => "er"
      case 'ㄧ' => "i"
      case 'ㄨ' => "u"
      case 'ㄩ' => "ü"
      case 'ㄫ' => "ng"
      case 'ㆠ' => "b"
      case 'ㆡ' => "j"
      case 'ㆢ' => "j"
      case 'ㆣ' => "g"
      case 'ㆤ' => "e"
      case 'ㆥ' => "enn"
      case 'ㆦ' => "oo"
      case 'ㆧ' => "onn"
      case 'ㆨ' => "ir"
      case 'ㆩ' => "ann"
      case 'ㆪ' => "inn"
      case 'ㆫ' => "unn"
      case 'ㆬ' => "m"
      case 'ㆭ' => "ng"
      case 'ㆮ' => "ainn"
      case 'ㆯ' => "aunn"
      case 'ㆰ' => "am"
      case 'ㆱ' => "om"
      case 'ㆲ' => "ong"
      case 'ㆴ' => "p"
      case 'ㆵ' => "t"
      case 'ㆶ' => "k"
      case 'ㆷ' => "h"
      case _ => ""
    }
      .mkString("").replace("ook","ok")
  }

}
