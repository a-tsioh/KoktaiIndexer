package controllers


import java.nio.ByteBuffer
import javax.inject.{Inject, Singleton}

import boopickle.Default._
//import controllers.PlayAutowire.{AutowireContext, AutowirePlayServer}

import play.api.http.{ContentTypeOf, ContentTypes}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class API @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def autowireApi(path: String) = Action.async(parse.raw) {
    implicit request =>
      // get the request body as ByteString
      val b = request.body.asBytes(parse.UNLIMITED).get
      // call Autowire route
      Router.route[api.SharedApi](models.ApiService)(
        autowire.Core.Request(path.split("/"), Unpickle[Map[String, ByteBuffer]].fromBytes(b.asByteBuffer))
      ).map(buffer => {
        val data = Array.ofDim[Byte](buffer.remaining())
        buffer.get(data)
        Ok(data)
      })
  }
}

