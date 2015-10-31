package controllers

import auth.SecuredAction

import play.api.mvc._
import auth.BasicAuth._

class Application extends Controller {

  def index = SecuredAction {
    Ok(views.html.index("Your new application is ready."))
  }
}
