package services.database

import auth.Credentials

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

class InMemoryDatabase extends Database {
  private val map = new TrieMap[String, String]()

  def storeValue(key: String, value: String): Unit = {
    map(key) = value
  }

  def lookupValue(key: String) = {
    map.get(key)
  }

  def allKeys() = {
    map.keys.toList
  }

  override def isUserExists(login: String): Future[Boolean] = {
    if(login != "vacia"){
      Future.successful(true)
    } else {
      Future.successful(false)
    }
  }

  override def getCredentials(login: String, pass: String): Future[Option[Credentials]] = {
    if (login == "vacia") {
      Future.successful(Some(Credentials(login)))
    } else {
      Future.successful(None)
    }
  }

}
