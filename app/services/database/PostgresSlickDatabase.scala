package services.database

import auth.Credentials
import org.apache.commons.dbcp2.BasicDataSource
import play.api.Logger
import slick.driver.PostgresDriver.api._
import slick.generated.Tables._
import utils.Utils._

import scala.concurrent.Future

class PostgresSlickDatabase(dataSource: BasicDataSource) extends MyDatabase {

  lazy val db: Database = Database.forDataSource(dataSource)

  override def regUser(login: String, pass: String): Future[Option[UserRegistered]] = {

    //ok, that works fine
    import play.api.libs.concurrent.Execution.Implicits._
    val xx = db.run(Account.result).map(_.foreach {
      case row => Logger.debug(s"${row.name} : ${row.passhash}")
    })


    val id = 666//this number does't participate in sql query (don't know why slick generate id:Int, not a id:Option[Int]
    val query = Account += AccountRow(id, login, sha256(pass))

    db.run(query)//does nothing...
//    db.run(query.result) does't compile :(


    Future.successful(Some(UserRegistered(login)))
  }

  override def getCredentials(login: String, pass: String): Future[Option[Credentials]] = {
    //todo
    Future.successful(Some(Credentials(login)))
  }
}
