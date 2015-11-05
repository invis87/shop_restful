package auth

import play.api.mvc._
import play.api.mvc.Results._
import services.database.MyDatabase

import scala.concurrent.Future

trait Secured {
  def db : MyDatabase

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def AuthAPI = new AuthAction(onUnauthorizedAPI, onServiceErrorAPI)

  class AuthAction(onUnauthorized: (Request[_] => Result), onServerError: (Exception => Result))
    extends ActionBuilder[AuthWrappedRequest] with ActionRefiner[Request, AuthWrappedRequest] {
    def refine[A](request: Request[A]) = verifyRequestAsync(request).map {
      case Some(credentials) => Right(AuthWrappedRequest(credentials, request))
      case None => Left(onUnauthorized(request))
    } recover {
      case e: Exception => Left(onServerError(e))
    }
  }

  def onUnauthorizedAPI(request: RequestHeader) = Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")

  def onServiceErrorAPI(e: Exception) = InternalServerError("External service or network error")

  def verifyRequestAsync[A](request: Request[A]): Future[Option[Credentials]] = {
    val credentialsOption = request.headers.get("Authorization").flatMap(str => str.split(" ").drop(1).headOption)
    credentialsOption.map(filterEncodedLoginPass).getOrElse(Future.successful(None))
  }

  def filterEncodedLoginPass(encodedLogPass: String): Future[Option[Credentials]] = {
    new String(org.apache.commons.codec.binary.Base64.decodeBase64(encodedLogPass.getBytes)).split(":").toList match {
      case u :: p :: Nil => db.getCredentials(u, p)
      case _ => Future.successful(None)
    }
  }
}

