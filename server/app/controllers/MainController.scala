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
                <div class="ui icon input" id="search_input">
                  <i class="search link icon"></i>
                </div>
              </div>
              <div class="ui inverted right menu">
                <a href="#" class="item" id="button_about">關於</a>
                <a href="#" class="item">代序</a>
                <a href="#" class="item">聯絡</a>
              </div>
            </div>
          </div>
          <div class="ui grid">
            <div class="four wide column">
             <div class="ui bottom attached segment" id="sidebar">
              </div>
            </div>
            <div class="twelve wide column">
              <div class="ui shape">
                <div class="sides">
                  <div class="side active" id="side_about">
                    <div class="ui transparent main text container">
                      <div class="ui transparent vertical masthead center aligned segment">
                        <h1 class="ui inverted header">國臺對照活用辭典</h1>
                        <h2 class="ui inverted header">原作者為吳守禮教授</h2>

                          <div class="ui segment">
                            <p>除原始資料外，此檔案庫內轉換格式、重新編排的編輯著作權（如果有的話）皆以 CC0 釋出，衍生著作物應以原始資料之授權為準。</p>
                          <p>《吳守禮國台對照活用辭典》作者：吳守禮（Ngo ShuLeh、Wu Shouli） ，本網站資料由吳守禮家族授權中華民國維基媒體協會，以創用CC 姓名標示-相同方式分享 3.0 台灣 授權條款釋出。</p>
                            <div class="ui image"><img src="/assets/images/by-sa.svg"></img></div>
                          </div>
                      </div>
                    </div>
                  </div>
                  <div class="side" id="side_reader">
                    <div class="ui transparent main text container" id="main_zone">
                      <h1 class="ui inverted header">辭典內容</h1>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <script src="/assets/client-fastopt.js" type="text/javascript">

          </script>
        </body>
      </html>
    Ok(html).as(HTML)
  }

}
