package chat

import akka.actor._
import akka.http.scaladsl.Http
import akka.routing._
import akka.stream.ActorMaterializer

import chat.db._

object Server extends ChatService {
  def main(args: Array[String]) = {
    implicit val system = ActorSystem("akka-http-chat")
    implicit val materializer = ActorMaterializer()
    println("Starting up....")
    system.actorOf(Props[DatabaseActor].withRouter(RoundRobinPool(10)), "database-actor")
    Http().bindAndHandle(chatRoute, "localhost", 8080)
  }
}
