package chat.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._

trait ConversationService {

  def getConversation = ???

  def getAllConversations = ???
}
