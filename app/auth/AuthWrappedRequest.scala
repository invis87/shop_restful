package auth

import play.api.mvc.Request

case class AuthWrappedRequest[A](credentials: Credentials, request: Request[A])
