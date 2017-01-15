package chat.actor

import akka.actor._

import chat.model._
import chat.util._

class ConversationActor(conversation: Conversation) extends Actor {
  var activeUsers: Map[String, ActorRef] = Map()

  val databaseActor = context.actorSelection("/user/database-actor")
  
  def receive = {
    case WsConnectionOpen(user) =>
      activeUsers += ((user, sender()))
    case WsConnectionClosed(user) =>
      activeUsers - user
    case msg: IncomingMessage =>
      val unreadBy = conversation.users.diff(activeUsers.keys.toSet)
      val timestamp = new java.util.Date().getTime()
      val message = Message(timestamp, conversation.uuid, msg.sender, conversation.users, msg.content, unreadBy)
      
      databaseActor ! StoreMessage(message)
      
      activeUsers.foreach { 
        case (k, v) => v ! msg 
      }
    case _ =>
  }
}
