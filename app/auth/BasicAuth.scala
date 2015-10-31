package auth

import auth.source.AuthSource
import play.api.mvc._

object BasicAuth extends Controller {

  implicit def authNew(block: => Result, source: AuthSource): Action[AnyContent] = Action(BodyParsers.parse.default) { request =>
    val authHead = request.headers.get("Authorization")
    val authOption = authHead.flatMap(str => str.split(" ").drop(1).headOption.filter(filterEncodedLoginPass(_, source)))
    authOption.map(_ => block).getOrElse(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
  }

  def filterEncodedLoginPass(str: String, source: AuthSource): Boolean = {
    new String(org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes)).split(":").toList match {
      case u :: p :: Nil => source.isPermit(u, p)
      case _ => false
    }
  }
}
