package utils

object TsuIm {
  val tones = "ㆴㆵㆶㆷˊˇˋ˫˪ ͘".toCharArray.toSet
  case class Syl(seg:String,tone:String)
  def splitTone(s:String): Syl = {
    val last: Char = s.last
    if(tones.contains(last)) {
      if(last == '͘') Syl(s.dropRight(3),s.takeRight(3).replace(" ",""))
      else Syl(s.dropRight(1), s.takeRight(1))
    }
    else Syl(s,"")
  }

}
