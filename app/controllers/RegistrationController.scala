package controllers

import auth.Secured
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import services.database.{UserRegistered, MyDatabase}

import scala.concurrent.Future

import scala.language.postfixOps

class RegistrationController(database: MyDatabase) extends Controller with Secured {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  override def db: MyDatabase = database

  def userCreated(login: String) =  Ok(Json.obj("success" -> s"$login created"))
  def userAlreadyExists(login: String) = Conflict(Json.obj("fail" -> s"user $login already exists"))
  val somethingBadHappens = ServiceUnavailable(Json.obj("fail" -> "something bad happens, try again later"))
  val missingParameter = BadRequest(Json.obj("fail" -> "missing parameter"))

  val loginAndPassValidator = (
    (__ \ 'login).read[String] and
      (__ \ 'password).read[String]
    ) tupled

  def reg = Action.async(parse.json) { request =>
    request.body.validate(loginAndPassValidator).map {
      case (login, pass) => tryCreateUser(login, pass)
    }.recoverTotal(_ => Future.successful(missingParameter))
  }

  def tryCreateUser(login: String, pass: String) = {
    db.regUser(login, pass).map {
      case Some(UserRegistered(log)) => userCreated(log)
      case None => userAlreadyExists(login)
    }.recover {
      case _ => somethingBadHappens
    }
  }

}
