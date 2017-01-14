package chat

import akka.util.Timeout
import scala.concurrent.duration._

package object http {
  protected [http] implicit val timeout = Timeout(30 seconds)
}
