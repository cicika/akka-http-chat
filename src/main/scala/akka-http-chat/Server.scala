package chat

import akka.actor._
import akka.http.scaladsl.Http
import akka.routing._
import akka.stream.ActorMaterializer

import com.typesafe.config.ConfigFactory

import chat.actor._
import chat.db._

object Server extends ChatService {
  def main(args: Array[String]):Unit = {
    implicit val system = ActorSystem("akka-http-chat")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    println("Starting up....")
    val config = ConfigFactory.load()
    database.create()

    system.actorOf(Props[DatabaseActor].withRouter(RoundRobinPool(10)), "database-actor")
    system.actorOf(Props[ConversationSupervisorActor].withRouter(RoundRobinPool(20)), "conversation-supervisor")
    Http().bindAndHandle(chatRoute(system), "localhost", 8080)
  }
}
