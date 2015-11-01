package services.akka

import play.api.Play.current
import play.api.libs.concurrent.Akka

trait ActorsModule {

  lazy val actorSystem = Akka.system
}
