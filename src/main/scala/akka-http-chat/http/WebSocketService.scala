package chat.http

import akka.actor._
import akka.actor.ActorRefProvider
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message => WsMessage, TextMessage}
import akka.NotUsed
import akka.pattern._
import akka.stream._
import akka.stream.scaladsl._

import chat.actor._
import chat.model._
import chat.util._

import scala.concurrent._
import scala.util.{Success, Failure}

trait WebSocketService extends ChatJsonProtocol {
  def webSocketHandler(params: Map[String, String],
                       user: User, 
                       system: ActorSystem): Flow[WsMessage, WsMessage, NotUsed] = {
    val conversationUuid = java.util.UUID.fromString(params("conversation"))
    
    val userWsActor =
      system.actorOf(Props(
        new UserWsActor(conversationUuid, user)), s"user-ws-actor-$conversationUuid-$user.name")

    val incomingMessages: Sink[WsMessage, NotUsed] = Flow[WsMessage].map {
      case TextMessage.Strict(text) => IncomingMessage(conversationUuid, user.name, text)
    }.to(Sink.actorRef[IncomingMessage](userWsActor, PoisonPill))

    val outgoingMessages: Source[WsMessage, NotUsed] =
      Source.actorRef[OutgoingMessage](10, OverflowStrategy.fail)
            .mapMaterializedValue { outgoingActor =>
              userWsActor ! UserConnected(outgoingActor)
             NotUsed
      }.map((outMsg: OutgoingMessage) => TextMessage(s"$outMsg.sender: $outMsg.content"))
  
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)    
  }
}    
