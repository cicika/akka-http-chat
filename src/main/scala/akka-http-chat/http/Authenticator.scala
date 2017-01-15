package chat.http

import akka.actor._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

import chat.db._
import chat.model._
import chat.util._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

trait ChatAuthenticator {
  val authenticate: Directive1[User] = {
    optionalHeaderValueByName("Authorization") flatMap {
      case Some(authHeader) =>
        val accessToken = java.util.UUID.fromString(authHeader.split(' ').last)
        onSuccess(database.Users.byUuid(accessToken)).flatMap {
          case Some(user) => 
            log.debug(s"Acquired user $user")
            provide(user)
          case _ => complete {
            StatusCodes.Unauthorized
          }
        }
      case _ => complete {
        StatusCodes.Unauthorized
      }
    }
  }
}
