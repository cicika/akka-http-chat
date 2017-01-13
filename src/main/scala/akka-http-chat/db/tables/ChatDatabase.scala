package chat.db

import com.outworkers.phantom.connectors
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import tables._

class ChatDatabase(override val connector: CassandraConnection)
  extends Database[ChatDatabase](connector) {
    object users extends UsersTable with connector.Connector
    object messages extends MessagesTable with connector.Connector
    object unreadMessages extends UnreadMessagesTable with connector.Connector
    object conversations extends ConversationsTable with connector.Connector
}

object DB extends ChatDatabase(connector)
