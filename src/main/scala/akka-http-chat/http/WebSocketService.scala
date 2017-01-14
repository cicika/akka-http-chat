package chat.http

import akka.actor._
import akka.pattern._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import chat.model._
import chat.util._

import scala.concurrent._
import scala.util.{Success, Failure}

trait WebSocketService extends ChatJsonProtocol {
  def webSocketHandler = (user: User, databaseActor: Future[ActorRef]) => {???}    
}