package chat

import akka.actor.{Actor, ActorContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import http._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait ChatService extends UserService
                     with ConversationService
                     with MessageService {

  def chatRoute = openRoute ~ conversationsRoute

  def openRoute = {
    post {
      path("users"){
        postUser
      }
    }
  }

  def conversationsRoute(uuid: java.util.UUID) = {
    get {
      path("conversations"){
       getAllConversations
      }
    } ~
    get {
      path("conversations" / Segment){
        getConversation
      }
    } 
  } 
}
