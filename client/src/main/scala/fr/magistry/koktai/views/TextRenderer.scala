package fr.magistry.koktai.views


import org.scalajs.dom.html.Div
import scalatags.JsDom
import scalatags.JsDom.all._


object TextRenderer {
  val $ = scala.scalajs.js.Dynamic.global.$

  def divOfText(t: Seq[api.Text]): JsDom.TypedTag[Div] = {
    div(t.map {
      case api.SimpleText(t) => raw(t)
      case api.Ruby(r, img) =>
        val syl = utils.TsuIm.splitTone(r.trim)
        div(
          cls := "ruby_chr with_popup",
          data("html"):= "<div class='content'><img src=\"/assets/" + img + "\"></img></div>",
          div(cls := "ruby_syl", syl.seg),
          div(cls := "ruby_tone", syl.tone)
        )
      case api.KoktaiCJK(cjk, img) =>
        span(
          cls:= "with_popup",
          data("html"):= "<div class='content'><img src=\"/assets/" + img + "\"></img></div>",
          cjk
        )
      case api.KoktaiIDS(ids, img) =>
        span(
          cls:= "with_popup",
          data("html"):= "<div class='content'><img src=\"/assets/" + img + "\"></img></div>",
          ids
        )
    })
  }

}
