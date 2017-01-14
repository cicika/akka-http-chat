package chat.db

import com.outworkers.phantom.connectors
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import tables._

class ChatDatabase(override val connector: CassandraConnection)
  extends Database[ChatDatabase](connector) {
    object Users extends ConcreteUsers with connector.Connector
    object Messages extends ConcreteMessages with connector.Connector
    object UnreadMessages extends ConcreteUnreadMessages with connector.Connector
    object Conversations extends ConcreteConversations with connector.Connector
}

object ChatDatabase extends ChatDatabase(connector)

trait ChatDbProvider extends DatabaseProvider[ChatDatabase] {
  override def database: ChatDatabase = ChatDatabase
}
