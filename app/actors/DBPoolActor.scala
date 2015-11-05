package actors

import java.sql.SQLException

import akka.actor._
import akka.pattern._
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.typesafe.config._
import org.apache.commons.dbcp2.BasicDataSource
import play.api.Logger
import slick.driver.PostgresDriver.api._
import slick.generated.Tables._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object DBPoolActor {
  case class CreateAccount(login: String, pass: String)
  case class CheckAccount(login: String, pass: String)
  case class GetStore()
}

class DBPoolActor extends Actor {

  implicit val timeout = Timeout(2 seconds)

  private val propertiesFile: String = "DBConnection.properties"
  private val conf = ConfigFactory.load(propertiesFile)

  private val workersStrategy: SupervisorStrategy = OneForOneStrategy(10, 1 minute, loggingEnabled = true) {
    case e: SQLException =>
      Logger.error(s"SQLException on child ${sender().path.name}", e)
      SupervisorStrategy.restart
    case _: ActorInitializationException => SupervisorStrategy.restart
    case _ => SupervisorStrategy.escalate
  }

  private val dataSource: BasicDataSource = initConnectionPool(conf)
  private lazy val dbWorkersRouter: ActorRef = context.actorOf(
    new RoundRobinPool(12).
      withSupervisorStrategy(workersStrategy).
      props(DBWorkerActor.props(dataSource)).
      withDispatcher("db-dispatcher"),
    "dbWorkersRouter")

  context.watch(dbWorkersRouter)

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)

    dataSource.close()
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()

    dataSource.close()
  }

  def initConnectionPool(conf: Config): BasicDataSource = {
    val host = conf.getString("host")
    val port = conf.getInt("port")
    val database = conf.getString("database")
    val urlStartsWith = conf.getString("dbConnectionUrlStartsWith")
    val connectionPool = new BasicDataSource()

    connectionPool.setDriverClassName(conf.getString("driverClassname"))
    connectionPool.setUrl(s"$urlStartsWith$host:$port/$database")

    connectionPool.setUsername(conf.getString("username"))
    connectionPool.setPassword(conf.getString("password"))

    connectionPool.setInitialSize(conf.getInt("poolSize"))
    connectionPool.setMaxConnLifetimeMillis(conf.getInt("connLifetimeMillis"))
    connectionPool.setMaxTotal(conf.getInt("maxTotal"))
    connectionPool.setMaxIdle(conf.getInt("maxIdle"))
    connectionPool.setMaxWaitMillis(conf.getInt("maxWaitMillis"))

    connectionPool.setRemoveAbandonedOnMaintenance(conf.getBoolean("removeAbandoned"))
    connectionPool.setRemoveAbandonedTimeout(conf.getInt("removeAbandonedTimeout"))
    connectionPool.setLogAbandoned(conf.getBoolean("logAbandoned"))

    connectionPool
  }

  import context.dispatcher

  override def receive: Receive = {
    case msg =>
      val s = sender()
      (dbWorkersRouter ask msg).map(answer => s ! answer)
  }
}
