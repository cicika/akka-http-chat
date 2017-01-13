package chat.db.tables

import chat.model._
import com.outworkers.phantom.dsl._
import scala.concurrent.Future

class UsersTable extends CassandraTable[ConcreteUsers, User] {
  object uuid extends UUIDColumn(this) with PartitionKey
  object email extends StringColumn(this)
  object name extends StringColumn(this)
}

abstract class ConcreteUsers extends UsersTable with RootConnector {
  def store(user: User): Future[ResultSet] = {
    insert.value(_.uuid, user.uuid)
          .value(_.email, user.email)
          .value(_.name, user.name)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
  }

  def byUuid(uuid: java.util.UUID): Future[Option[User]] = {
    select.where(_.uuid eqs uuid).one()
  }
}
