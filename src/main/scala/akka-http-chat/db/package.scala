package chat

import com.outworkers.phantom.dsl._

package object db {
  protected [db] val hosts = Seq("127.0.0.1")
  protected [db] val Connector = ContactPoints(hosts).keySpace("akka-http-chat")
}
