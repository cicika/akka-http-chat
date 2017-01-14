package chat.model

case class CreateUser(user: User)
case class FetchUser(uuid: java.util.UUID)

case class FetchMessages(conversationOpt: Option[Conversation] = None, userOpt: Option[User] = None)
case class FetchUnreadMessages(conversation: Conversation, user: User)
case class MarkMessagesRead(conversation: Conversation, user: User)

case class FetchConversations(user: Option[User])
case class FetchConversation(uuid: java.util.UUID)
