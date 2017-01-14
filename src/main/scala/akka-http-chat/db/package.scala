package chat

import com.outworkers.phantom.dsl._
import com.outworkers.phantom.dsl.context

package object db extends ChatDbProvider {
  protected [db] val hosts = Seq("127.0.0.1")
  protected [db] val connector = ContactPoints(hosts).keySpace("akka_http_chat")
}
