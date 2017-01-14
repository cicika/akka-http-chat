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

  def getConversation = (uuid: java.util.UUID, databaseActor: Future[ActorRef]) => {
    onComplete(databaseActor) {
      case Success(actor) =>
        onComplete((actor ? FetchMessages(Some(Conversation(uuid, Set[String]())), None)).mapTo[List[Message]]) {
          case Success(messages) if messages.isEmpty =>
            complete {
              StatusCodes.NotFound
            }
          case Success(messages) =>
            complete(messages)
          case Failure(ex) =>
            log.error(s"Something exploded in the database for conversation $uuid")
            complete {
              StatusCodes.InternalServerError
            }
        }
      case Failure(ex) =>
        log.error("Could not acquire database actor!")
        complete {
          StatusCodes.InternalServerError
        }
    }
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
            complete(messages)
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
