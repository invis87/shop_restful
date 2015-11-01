package controllers

import auth.Secured
import play.api.mvc.{BodyParsers, Controller}
import services.database.Database

import scala.concurrent.Future

class MainController(db: Database) extends Controller with Secured {

  def index = AuthAPI.async(BodyParsers.parse.anyContent) { req =>
    Future.successful(Ok(views.html.index(s"Hello ${req.credentials.login}, your new application is ready.")))
  }

  override def userDB: Database = db
}
