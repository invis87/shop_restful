package controllers

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import services.database.Database

import scala.concurrent.Future

class RegistrationController(db: Database) extends Controller {

  val userAlreadyExists = Json.obj("problem" -> "user already exists")

  val loginAndPassValidator = (
    (__ \ 'login).read[String] and
      (__ \ 'password).read[String]
    ) tupled

  def reg = Action.async(parse.json) { request =>
    request.body.validate(loginAndPassValidator).map {
      case (login, pass) => tryCreateUser(login, pass)
    }.recoverTotal(_ => Future.successful(BadRequest(Json.obj("fail" -> "missing parameter"))))
  }

  def tryCreateUser(login: String, pass: String) = {

    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    db.userExists(login).map { exists =>
      if(exists) Conflict(userAlreadyExists)
      else Ok(Json.obj("success" -> "user created"))
    }
  }
}
