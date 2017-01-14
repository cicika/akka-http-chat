package chat.model

case class Message(
  timestamp: Long, 
  conversation: java.util.UUID,
  sender: String,
  content: String
){}

case class UnreadMessage(
  timestamp: Long,
  conversation: java.util.UUID,
  sender: String,
  recipients: Set[String],
  content: String
){}
