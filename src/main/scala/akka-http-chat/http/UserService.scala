package chat.http

import akka.actor._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import chat.model._
import chat.util._

import scala.concurrent._
import scala.util.{Success, Failure}

trait UserService extends ChatJsonProtocol {

  def postUser = (databaseActor: Future[ActorRef]) => {
    entity(as[BaseUser]) { u =>
      val user = u.toUser
      onComplete(databaseActor) {
        case Success(actor) =>
          actor ! CreateUser(user)
          complete(user)
        case Failure(ex) =>
          log.error("Could not acquire database actor!", ex)
          complete {
            StatusCodes.InternalServerError
          }
      }      
    }
  }
}
