package chat.http

import akka.actor._
import akka.pattern._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import chat.model._
import chat.util._

import scala.concurrent._
import scala.util.{Success, Failure}

trait ConversationService extends ChatJsonProtocol {

  def getConversation(uuid: java.util.UUID) = {
    complete {
      StatusCodes.NotImplemented
    }
  }

  def getAllConversations = (user: User, databaseActor: Future[ActorRef]) => {
    onComplete(databaseActor) {
      case Success(actor) =>
        onComplete((actor ? FetchMessages(None, Some(user))).mapTo[List[Option[Message]]]) {
          case Success(messages) =>
            // here, messages should be repacked to exclude extra data
            complete(messages.toVector)
          case Failure(ex) =>
            complete {
              StatusCodes.NotFound
            }
        }
      case Failure(ex) =>
        log.error("Could not acquire database actor!")
          complete {
            StatusCodes.InternalServerError
          }
    }
  }
}
