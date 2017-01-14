package chat.db

import akka.actor._

import chat.model._

import com.outworkers.phantom.dsl.context

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseActor extends Actor {

  def receive = userCommands orElse messageCommands orElse conversationCommands

  def userCommands: Receive = {
    case CreateUser(user) => database.Users.store(user)
    case FetchUser(uuid) =>
      val respondTo = sender()
      database.Users.byUuid(uuid) onSuccess {
        case result => respondTo ! result
      } 
  }

  def messageCommands: Receive = {
    case FetchMessages(Some(conversation), None) =>
      val respondTo = sender()
      database.Messages.forConversation(conversation) onSuccess {
        case result => respondTo ! result
      }
    case FetchMessages(None, Some(user)) =>
      val respondTo = sender()
      database.Messages.forUser(user) onSuccess {
        case result => respondTo ! result
      }
    case MarkMessagesRead(conversation, user) =>
      database.Messages.removeRecipientFromUnreadBy(conversation, user)
  }

  def conversationCommands: Receive = {
    case FetchConversations(user) => 

    case FetchConversation(uuid) =>
      val respondTo = sender()
      database.Conversations.byUuid(uuid) onSuccess {
        case result => respondTo ! result
      }
  }
}
