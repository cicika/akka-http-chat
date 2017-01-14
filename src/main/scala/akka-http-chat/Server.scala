package chat

import akka.actor._
import akka.http.scaladsl.Http
import akka.routing._
import akka.stream.ActorMaterializer

import com.typesafe.config.ConfigFactory

import chat.db._

object Server extends ChatService {
  def main(args: Array[String]):Unit = {
    implicit val system = ActorSystem("akka-http-chat")
    implicit val materializer = ActorMaterializer()

    println("Starting up....")
    val config = ConfigFactory.load()

    system.actorOf(Props[DatabaseActor].withRouter(RoundRobinPool(10)), "database-actor")
    
    Http().bindAndHandle(chatRoute(system), "localhost", 8080)
  }
}
