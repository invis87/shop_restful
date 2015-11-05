package services.akka

import com.softwaremill.macwire._
import actors.DBPoolActor
import akka.actor.Props
import play.api.Play.current
import play.api.libs.concurrent.Akka

trait ActorsModule {

  lazy val actorSystem = Akka.system
  lazy val dbPoolActor = actorSystem.actorOf(Props(wire[DBPoolActor])).taggedWith[DBPoolActor]
}
