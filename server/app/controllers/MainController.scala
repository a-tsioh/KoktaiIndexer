package controllers

import javax.inject._

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents}
import play.api.mvc._

@Singleton
class MainController @Inject()(cc: ControllerComponents, assets: Assets) extends  AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    val html =
      <html>
        <head>
           <script src="/assets/lib/jquery/dist/jquery.min.js"></script>
            <link rel={"stylesheet"} href={"/assets/lib/semantic-ui/dist/semantic.min.css"}/>
            <script src="/assets/lib/semantic-ui/dist/semantic.min.js"></script>
          <link rel={"stylesheet"} href={"/assets/css/style.css"}/>

        </head>
        <body>
          <div class="ui fixed inverted menu">
            <div class="ui container">
              <a href="#" class="header item">
                <i class="ui book icon" ></i>
                《國臺對照活用辭典》
              </a>
              <div class="ui item">
                <div class="ui icon input">
                  <input placeholder="Search..." type="text"></input>
                  <i class="search link icon"></i>
                </div>
              </div>
              <div class="ui inverted right menu">
                <a href="#" class="item">關於</a>
                <a href="#" class="item">代序</a>
                <a href="#" class="item">聯絡</a>
              </div>
            </div>
          </div>
          <div class="ui transparent main text container" >
            <div class="ui transparent vertical masthead center aligned segment">
              <h1 class="ui inverted header">國臺對照活用辭典</h1>
            </div>
          </div>
          <script src="/assets/client-fastopt.js" type="text/javascript">

          </script>
        </body>
      </html>
    Ok(html).as(HTML)
  }

}
