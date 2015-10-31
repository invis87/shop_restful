package controllers

import auth.SecuredAction

import play.api.mvc._
import scala.concurrent.Future
import auth.BasicAuth._

class Application extends Controller {

//  def index = Secured("vov", "vov") (
//    Action {
//      Ok(views.html.index("Your new application is ready."))
//    })

//  def index = SecuredAction.xz(
//    Action {
//    Ok(views.html.index("Your new application is ready."))
//  })


  def index = SecuredAction {
    Ok(views.html.index("Your new application is ready."))
  }

  def Secured[A](username: String, password: String)(action: Action[A]) = Action.async(action.parser) { request =>
    val authHead = request.headers.get("Authorization")
    if(authHead.isDefined) {
      println(authHead)
      //Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")

      val encoded = authHead.get.split(" ").drop(1).headOption
      val x = encoded.filter(filterEncodedLoginPass(_, username, password))

      if(x.isDefined){
        action(request)
      } else {
        Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
      }

    } else {
      Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
    }
  }

  def filterEncodedLoginPass(str: String, username: String, password: String): Boolean = {
    new String(org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes)).split(":").toList match {
      case u :: p :: Nil if u == username && password == p => true
      case _ => false
    }
  }
}
