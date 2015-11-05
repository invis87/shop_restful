import com.softwaremill.macwire._
import controllers.{MainController, RegistrationController}
import services.database.DatabaseModule

object Application extends DatabaseModule {
  val mainController          = wire[MainController]
  val registrationController  = wire[RegistrationController]
}
