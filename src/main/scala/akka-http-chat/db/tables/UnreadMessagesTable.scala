package chat.db.tables

import chat.model._
import com.outworkers.phantom.dsl._
import scala.concurrent.Future

class UnreadMessagesTable extends CassandraTable[ConcreteUnreadMessages, UnreadMessage] {
  object timestamp extends PrimitiveColumn[ConcreteUnreadMessages, UnreadMessage, Long](this) 
                   with PartitionKey
  object conversation extends UUIDColumn(this) with Index
  object sender extends StringColumn(this)
  object recipients extends ListColumn[String](this)
  object content extends StringColumn(this)
}

abstract class ConcreteUnreadMessages extends UnreadMessagesTable with RootConnector {
  def store(message: UnreadMessage): Future[ResultSet] = {
    insert.value(_.timestamp, message.timestamp)
          .value(_.conversation, message.conversation)
          .value(_.sender, message.sender)
          .value(_.recipients, message.recipients)
          .value(_.content, message.content)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
  }

  def forConversation(conversation: Conversation): Future[List[UnreadMessage]] = {
    select.where(_.conversation eqs conversation.uuid).fetch
  }

  def removeRecipient(conversation: Conversation, user: User) = {
    update.where(_.conversation eqs conversation.uuid)
          .modify(_.recipients discard user.name)
          .future()
  }
}
