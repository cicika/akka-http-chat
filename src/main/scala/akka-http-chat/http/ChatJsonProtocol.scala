package chat.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import chat.model._
import spray.json._

trait ChatJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object UuidJsonFormat extends RootJsonFormat[java.util.UUID] {
    def write(uuid: java.util.UUID) = JsString(uuid.toString)

    def read(value: JsValue) = value match {
      case JsString(uuid) => java.util.UUID.fromString(uuid)
      case x => deserializationError(s"Expected UUID as JsString, got $x")
    }
  }

  implicit val baseUserFormat = jsonFormat2(BaseUser)
  implicit val userFormat = jsonFormat3(User)
  implicit val messageFormat = jsonFormat6(Message)
  implicit val conversationFormat = jsonFormat2(Conversation)
}
