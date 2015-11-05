package actors

import java.sql.Connection

import akka.actor.{Actor, Props}
import auth.Credentials
import org.apache.commons.dbcp2.BasicDataSource
import play.api.Logger
import services.database.UserRegistered
import sql._

object DBWorkerActor {
  def props(db: BasicDataSource): Props = Props(new DBWorkerActor(db))
}

class DBWorkerActor(db: BasicDataSource) extends Actor {

  var connection: Connection = _

  def closeConnection() = {
    if(connection != null){
      connection.close()
    }
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()

    closeConnection()
    connection = db.getConnection
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()

    closeConnection()
  }

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)

    message.foreach {
      case DBPoolActor.CreateAccount(login, _) => sender ! None
    }

    closeConnection()
  }

  override def receive: Receive = {
    case DBPoolActor.CreateAccount(login, pass) => tryCreateAccount(login, pass)
    case DBPoolActor.CheckAccount(login, pass) => tryFoundAccount(login, pass)
  }

  private def tryCreateAccount(login: String, pass: String) = {
    Logger.info(s"Start creating account($login)")

    val connStatement = connection.createStatement()
    connStatement.executeUpdate(DBQueries.createAccount(login, pass))

    Logger.info(s"Account $login successfully created")

    connStatement.close()

    sender ! Some(UserRegistered(login))
  }

  private def tryFoundAccount(login: String, pass: String) = {
    val connStatement = connection.createStatement()
    val result = connStatement.executeQuery(DBQueries.foundAccount(login, pass))

    if (result.next()) {
      sender ! Some(Credentials(login))
    } else {
      sender ! None
    }

    connStatement.close()
  }
}
