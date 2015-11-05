package services.database

import actors.DBPoolActor
import akka.actor.ActorRef
import akka.util.Timeout
import auth.Credentials
import business.Store
import com.softwaremill.macwire._

import scala.concurrent.Future
import akka.pattern._
import scala.concurrent.duration._

import scala.language.postfixOps

class PostgresDatabase(dBPoolActor: ActorRef @@ DBPoolActor) extends MyDatabase {

  private implicit val timeout = Timeout(2 seconds)

  override def getCredentials(login: String, pass: String): Future[Option[Credentials]] = {
    dBPoolActor.ask(DBPoolActor.CheckAccount(login, pass)).mapTo[Option[Credentials]]
  }

  override def regUser(login: String, pass: String): Future[Option[UserRegistered]] = {
    dBPoolActor.ask(DBPoolActor.CreateAccount(login, pass)).mapTo[Option[UserRegistered]]
  }
}
