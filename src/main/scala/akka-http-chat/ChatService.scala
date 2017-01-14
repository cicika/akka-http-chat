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
                     with ChatAuthenticator {

  implicit val timeout = Timeout(5 seconds)

  def chatRoute(sys: ActorSystem) = openRoute(sys) ~
                                    authenticateBasicAsync(realm = realm, authenticator) {
                                      user => conversationsRoute(user, sys)
                                    } ~ complete(StatusCodes.NotFound)

  def openRoute = (sys: ActorSystem) => {
    post {
      path("users"){
        postUser(sys.actorSelection("/user/database-actor").resolveOne)
      }
    }
  }

  def conversationsRoute(user: User, sys: ActorSystem) = {
    get {
      path("conversations"){
       getAllConversations(user, sys.actorSelection("database-actor").resolveOne)
      }
    } ~
    get {
      path("conversations" / JavaUUID){ uuid =>
        getConversation(uuid, sys.actorSelection("database-actor").resolveOne)
      }
    }
  }
}
