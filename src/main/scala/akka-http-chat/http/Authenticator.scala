package chat.http

import akka.actor._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.directives._
import akka.pattern._
import akka.util.Timeout

import chat.db._
import chat.model._
import chat.util._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

trait ChatAuthenticator {
  def realm = "AkkaHttpChat Application Realm"
  def scheme = "ChatUser"

  def authenticator(credentials: Credentials): Future[Option[User]] = {
    val result = credentials match {
      case p @ Credentials.Provided(creds) =>
        if(creds.toString.startsWith(scheme)) {
          // calling here database directly, shouldn't
          database.Users.byUuid(java.util.UUID.fromString(creds.toString.drop(scheme.length+1)))
        }
        else {
          Future(None)
        }
      case _ => Future(None)      
    }
    result
  }
}
