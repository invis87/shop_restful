package services.database

import scala.concurrent.Future

trait Database {
  def storeValue(key: String, value: String): Unit
  def lookupValue(key: String): Option[String]
  def allKeys(): List[String]

  def userExists(login: String): Future[Boolean]
}
