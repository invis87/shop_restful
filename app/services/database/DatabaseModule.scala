package services.database

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.commons.dbcp2.BasicDataSource
import services.akka.ActorsModule

trait DatabaseModule extends ActorsModule {
  lazy val conf: Config = ConfigFactory.load("DBConnection.properties")
  lazy val database: MyDatabase = new PostgresSlickDatabase(initConnectionPool(conf))

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
}
