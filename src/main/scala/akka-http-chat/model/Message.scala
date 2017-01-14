package chat.model

import java.util.UUID

case class Message(
  timestamp: Long, 
  conversation: UUID,
  sender: String,
  content: String
){}

case class UnreadMessage(
  timestamp: Long,
  conversation: UUID,
  sender: String,
  recipients: Set[String],
  content: String
){
  def toMessage = Message(timestamp, conversation, sender, content)
}
