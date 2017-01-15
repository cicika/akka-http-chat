package chat.actor

import akka.actor._

import chat.model._
import chat.util._

class UserWsActor(conversationUuid: java.util.UUID, user: User) extends Actor {

  val conversationActor = context.actorSelection(s"/user/conversation-supervisor/conversation-$conversationUuid")

  def receive = {
    case UserConnected(outgoingActor) =>
      conversationActor ! WsConnectionOpen(user.name)
      context.become(connected(outgoingActor))
    case _ =>
  }

  def connected(outgoingActor: ActorRef): Receive = {
    case msg: IncomingMessage =>
      conversationActor ! msg
    case msg: Message =>
      outgoingActor ! OutgoingMessage(msg.sender, msg.content)
  }
}
