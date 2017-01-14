package chat.db.tables

import chat.model._
import com.outworkers.phantom.dsl._
import java.util.UUID
import scala.concurrent.Future

class ConversationsTable extends CassandraTable[ConcreteConversations, Conversation] {
  object uuid extends UUIDColumn(this) with PartitionKey
  object users extends SetColumn[String](this) with Index
}

abstract class ConcreteConversations extends ConversationsTable with RootConnector {
  def store(conversation: Conversation): Future[ResultSet] = {
    insert.value(_.uuid, conversation.uuid)
          .value(_.users, conversation.users)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
  }

  def byUuid(uuid: java.util.UUID): Future[Option[Conversation]] = {
    select.where(_.uuid eqs uuid).one()
  }

  def forUser(user: User): Future[List[Conversation]] = {
    select.where(_.users contains user.name).fetch()
  } 
}
