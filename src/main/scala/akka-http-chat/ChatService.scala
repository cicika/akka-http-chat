package chat

import akka.actor.{Actor, ActorContext, ActorSystem}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout

import http._
import model._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait ChatService extends UserService
                     with ConversationService
                     with ChatAuthenticator
                     with WebSocketService {

  implicit val timeout = Timeout(5 seconds)

  def chatRoute(system: ActorSystem) =
    openRoute(system) ~
    authenticate { user =>
      conversationsRoute(user, system)
    } ~
    authenticate { user =>
      wsRoute(user, system)
    }

  def openRoute = (system: ActorSystem) => {
    post {
      path("users") {
        postUser(system.actorSelection("/user/database-actor").resolveOne)
      }
    }
  }

  def conversationsRoute(user: User, system: ActorSystem) = {
    post {
      path("conversations") {
        createConversation(user, system.actorSelection("/user/conversation-supervisor").resolveOne)
      }
    } ~
    get {
      path("conversations") {
       getAllConversations(user, system.actorSelection("/user/database-actor").resolveOne)
      }
    } ~
    get {
      path("conversations" / JavaUUID){ uuid =>
        getConversation(uuid, system.actorSelection("/user/database-actor").resolveOne)
      }
    }
    
  }

  def wsRoute(user: User, system: ActorSystem) = {
    get {
      path("chat") {
        parameterMap { params =>
          handleWebSocketMessages(
            webSocketHandler(params, user, system)
          )
        }
      }
    }
  }
}
