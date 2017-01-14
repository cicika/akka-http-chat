package chat.model

import java.util.UUID

case class Conversation(
  uuid: UUID = UUID.randomUUID(),
  users: Set[String]
) {
  def addUser(user: User): Conversation = {
    Conversation(uuid, users + user.name)
  }
}
