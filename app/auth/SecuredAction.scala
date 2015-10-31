package auth

import auth.source.AuthSource
import play.api.mvc._

object SecuredAction {

  //authentication method
  import auth.source.Implicits.Simple

  def apply(block: => Result)(implicit auth: (=> Result, AuthSource) => Action[AnyContent]) = {
    auth(block, implicitly[AuthSource])
  }
}
