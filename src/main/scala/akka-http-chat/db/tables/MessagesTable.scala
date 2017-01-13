package chat.db.tables

import chat.model._
import com.outworkers.phantom.dsl._
import scala.concurrent.Future

class MessagesTable extends CassandraTable[ConcreteMessages, Message] {
  object timestamp extends PrimitiveColumn[ConcreteMessages, Message, Long](this) with PartitionKey
  object conversation extends UUIDColumn(this) with Index
  object sender extends StringColumn(this)
  object content extends StringColumn(this)
}

abstract class ConcreteMessages extends MessagesTable with RootConnector {
  def store(message: Message): Future[ResultSet] = {
    insert.value(_.timestamp, message.timestamp)
          .value(_.conversation, message.conversation)
          .value(_.sender, message.sender)
          .value(_.content, message.content)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
  }

  def forConversation(conversation: Conversation): Future[List[Message]] = {
    select.where(_.conversation eqs conversation.uuid).fetch
  }
}