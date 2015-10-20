package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import dao.MyModelDAO

class Application extends Controller {

  def index = Action.async {
    val myModelDAO = new MyModelDAO
    val action = myModelDAO.findByName("name1")

    myModelDAO.run(action).map(res => {
      Ok("ok")
    })
  }

}
