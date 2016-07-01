package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import dao.BookDAO

class Application extends Controller {

  def index = Action.async {
    val bookDAO = new BookDAO
    val action = bookDAO.findByName("name1")

    bookDAO.run(action).map(res => {
      Ok(res(0).name)
    })
  }

}
