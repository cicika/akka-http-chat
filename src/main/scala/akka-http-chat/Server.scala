package chat

import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Server extends ChatService {
  def main(args: Array[String]) = {
    implicit val system = ActorSystem("akka-http-chat")
    implicit val materializer = ActorMaterializer()
    println("Starting up....")
    Http().bindAndHandle(chatRoute, "localhost", 8080)
  }
}
