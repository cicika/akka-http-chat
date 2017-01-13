package chat.model

case class CreateUser(user: User)
case class FetchUser(uuid: java.util.UUID)

case class FetchMessages(conversation: Conversation)
case class FetchUnreadMessages(conversation: Conversation, user: User)
case class MarkMessagesRead(conversation: Conversation, user: User)
case class MoveFromUnreadToMessages(conversation: Conversation)

case class FetchConversations(user: Option[User])
case class FetchConversation(conversation: Conversation)
