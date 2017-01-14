package chat.model

import java.util.UUID

case class Message(
  timestamp: Long, 
  conversation: UUID,
  sender: String,
  recipients: Set[String],
  content: String,
  unreadBy: Set[String]
){}
