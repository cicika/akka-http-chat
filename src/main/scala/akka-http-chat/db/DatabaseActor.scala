package chat.db

import akka.actor._

import chat.model._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseActor extends Actor {

  def receive = userCommands orElse messageCommands orElse conversationCommands

  def userCommands: Receive = {
    case CreateUser(user) => 
    case FetchUser(uuid) => 
  }

  def messageCommands: Receive = {
    case FetchMessages(conversation) =>
    case FetchUnreadMessages(conversation, user) =>
    case MarkMessagesRead(conversation, user) =>
    case MoveFromUnreadToMessages(conversation) =>
  }

  def conversationCommands: Receive = {
    case FetchConversations(Some(user)) =>
    case FetchConversation(conversation) =>
  }
}
