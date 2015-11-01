package services.database

import auth.Credentials

import scala.concurrent.Future

trait Database {
  def storeValue(key: String, value: String): Unit
  def lookupValue(key: String): Option[String]
  def allKeys(): List[String]

  def isUserExists(login: String): Future[Boolean]
  def getCredentials(login: String, pass: String): Future[Option[Credentials]]
}
