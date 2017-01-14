package chat

import akka.actor.{Actor, ActorContext, ActorSystem}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout

import http._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait ChatService extends UserService
                     with ConversationService
                     with MessageService {

  implicit val timeout = Timeout(5 seconds)

  def chatRoute(sys: ActorSystem) = openRoute(sys) ~ conversationsRoute

  def openRoute = (sys: ActorSystem) => {
    post {
      path("users"){
        postUser(sys.actorSelection("database-actor").resolveOne)
      }
    }
  }

  def conversationsRoute = {
    get {
      path("conversations"){
       getAllConversations
      }
    } ~
    get {
      path("conversations" / JavaUUID){ uuid =>
        getConversation(uuid)
      }
    } 
  } 
}
