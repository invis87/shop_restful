package auth

import auth.source.AuthSource
import play.api.mvc._

import scala.concurrent.Future

object BasicAuth extends Controller {

  //todo: remove when finish project
//  implicit def auth[A](username: String, password: String, action: Action[A]): Action[A] = Action.async(action.parser) { request =>
//    val authHead = request.headers.get("Authorization")
//    if(authHead.isDefined) {
//      val encoded = authHead.get.split(" ").drop(1).headOption
//      val x = encoded.filter(filterEncodedLoginPass(_, username, password))
//
//      if(x.isDefined){
//        action(request)
//      } else {
//        Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
//      }
//
//    } else {
//      Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
//    }
//  }

  implicit def authNew(block: => Result, source: AuthSource): Action[AnyContent] = Action.async(BodyParsers.parse.default) { request =>
    val authHead = request.headers.get("Authorization")
    if(authHead.isDefined) {
      val encoded = authHead.get.split(" ").drop(1).headOption
      val x = encoded.filter(filterEncodedLoginPass(_, source))

      if(x.isDefined){
        Future.successful(block)
      } else {
        Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
      }

    } else {
      Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
    }
  }

  def filterEncodedLoginPass(str: String, source: AuthSource): Boolean = {
    new String(org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes)).split(":").toList match {
      case u :: p :: Nil => source.isPermit(u, p)
      case _ => false
    }
  }
}
