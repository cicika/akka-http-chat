package chat.actor

import akka.actor._
import akka.pattern._
import akka.util.Timeout

import chat.model._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ConversationSupervisorActor extends Actor {
  implicit val timeout = Timeout(30 seconds)
  
  val databaseActor = context.actorSelection("/user/database-actor")
  
  def receive = {
    case CreateConversation(conversation) =>
      val respondTo = sender()
      databaseActor.resolveOne onSuccess {
        case act =>
          (act ? FetchConversationForUsers(conversation.users)).mapTo[Option[Conversation]] onSuccess {
            case Some(result) => respondTo ! result
            case None =>
              context.actorSelection("/user/database-actor") ! CreateConversation(conversation)
              val conversationActor = context.actorOf(Props[ConversationActor], s"conversation-$conversation.uuid")
              conversationActor ! conversation
              context.watch(conversationActor)
          }
      }
      
    case _ =>
  }
}
