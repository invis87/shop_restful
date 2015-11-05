package services.database

import auth.Credentials
import business.Store

import scala.concurrent.Future

trait MyDatabase {
  def regUser(login: String, pass: String): Future[Option[UserRegistered]]
  def getCredentials(login: String, pass: String): Future[Option[Credentials]]
}

case class UserRegistered(login: String)