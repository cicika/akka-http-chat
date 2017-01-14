package chat.model

import java.util.UUID

case class BaseUser(
  email: String,
  name: String
){
  def toUser = User(UUID.randomUUID(), email, name)
}

case class User(
  uuid: UUID, 
  email: String, 
  name: String
){}
