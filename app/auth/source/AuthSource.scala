package auth.source

trait AuthSource {
  def isPermit(login: String, pass: String): Boolean
}

object Implicits {

  implicit object Simple extends AuthSource {
    private val login = "test"
    private val pass = "test"

    implicit override def isPermit(login: String, pass: String): Boolean = {
      this.login == login && this.pass == pass
    }
  }

  implicit object DB extends AuthSource {
    //todo
    override def isPermit(login: String, pass: String): Boolean = false
  }
}