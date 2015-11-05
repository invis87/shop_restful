package controllers

import auth.Secured
import play.api.mvc.{BodyParsers, Controller}
import services.database.MyDatabase

import scala.concurrent.Future

class MainController(database: MyDatabase) extends Controller with Secured {

  override def db: MyDatabase = database

  def index = AuthAPI.async(BodyParsers.parse.anyContent) { req =>
    Future.successful(Ok(views.html.index(s"Hello ${req.credentials.login}, your new application is ready.")))
  }
}
