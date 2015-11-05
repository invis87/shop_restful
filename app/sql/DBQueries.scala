package sql

import utils.Utils._

object DBQueries {

  def createAccount(login: String, pass: String) =
    s"""INSERT INTO "Account"(name, passhash) VALUES ('$login', '${sha256(pass)}');"""

  def foundAccount(login: String, pass: String) =
    s"""SELECT name FROM "Account" where name='$login' and passhash='${sha256(pass)}';"""
}
