package controllers

import auth.BasicAuth._
import auth.SecuredAction
import play.api.mvc.Controller


class MainController extends Controller {

  def index = SecuredAction {
    Ok(views.html.index("Your new application is ready."))
  }
}
