package chat.model

case class Conversation(
  uuid: java.util.UUID,
  users: Set[String]
) {
  def addUser(user: User): Conversation = {
    Conversation(uuid, users + user.name)
  }
}
