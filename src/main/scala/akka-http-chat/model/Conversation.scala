package chat.model

case class Conversation(
  uuid: java.util.UUID,
  users: List[String]
) {
  def addUser(user: User): Conversation = {
    Conversation(uuid, user.name :: users)
  }
}
