package chat.model

import akka.actor.ActorRef

case class CreateUser(user: User)
case class FetchUser(uuid: java.util.UUID)

case class StoreMessage(message: Message)
case class FetchMessages(conversationOpt: Option[Conversation] = None, userOpt: Option[User] = None)
case class FetchUnreadMessages(conversation: Conversation, user: User)
case class MarkMessagesRead(conversations: List[Conversation], user: User)

case class CreateConversation(conversation: Conversation)
case class FetchConversations(user: Option[User])
case class FetchConversation(uuid: java.util.UUID)
case class FetchConversationForUsers(users: Set[String])

case class WsConnectionOpen(user: String)
case class WsConnectionClosed(user: String)
case class UserConnected(outgoingActor: ActorRef)
