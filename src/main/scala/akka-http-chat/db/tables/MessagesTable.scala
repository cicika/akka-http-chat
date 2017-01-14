package chat.db.tables

import chat.model._
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.builder.query._
import scala.concurrent.Future

class MessagesTable extends CassandraTable[ConcreteMessages, Message] {
  object timestamp extends PrimitiveColumn[ConcreteMessages, Message, Long](this) with PrimaryKey
  object conversation extends UUIDColumn(this) with PartitionKey
  object sender extends StringColumn(this) with Index
  object recipients extends SetColumn[String](this) with Index
  object content extends StringColumn(this)
  object unreadBy extends SetColumn[String](this) with Index
}

abstract class ConcreteMessages extends MessagesTable with RootConnector {
  def store(message: Message): Future[ResultSet] = {
    insert.value(_.timestamp, message.timestamp)
          .value(_.conversation, message.conversation)
          .value(_.sender, message.sender)
          .value(_.recipients, message.recipients)
          .value(_.content, message.content)
          .value(_.unreadBy, message.unreadBy)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
  }

  def forConversation(conversation: Conversation): Future[List[Message]] = {
    select.where(_.conversation eqs conversation.uuid)
          .orderBy(_.timestamp desc)
          .fetch
  }

  def forUser(user: User): Future[List[Option[Message]]] = {
    for {
      asSender <-
        select.where(_.sender eqs user.name)
              .orderBy(_.timestamp desc)
              .one()
      asRecipient <-
        select.where(_.recipients contains user.name)
              .orderBy(_.timestamp desc)
              .one()
    } yield asSender :: List(asRecipient)
  }

  def unreadForUser(user: User): Future[List[Message]] = {
    select.where(_.unreadBy contains user.name)
          .fetch
  }

  def removeRecipientFromUnreadBy(conversations: List[Conversation], user: User) = {
    update.where(_.conversation in conversations.map(c => c.uuid))
          .modify(_.unreadBy remove user.name)
          .future()
  }
}
