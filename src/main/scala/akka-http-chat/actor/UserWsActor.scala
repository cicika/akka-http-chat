package chat.actor

import akka.actor._

import chat.model._
import chat.util._

class UserWsActor(conversationActor: ActorRef, outgoing: ActorRef) extends Actor {
  def receive = {
    case msg: IncomingMessage =>
      conversationActor ! msg
    case msg: Message =>
      outgoing ! OutgoingMessage(msg.sender, msg.content)
    case _ =>
  }
}
