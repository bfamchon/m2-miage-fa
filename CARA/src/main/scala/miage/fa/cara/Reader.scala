package miage.fa.cara

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.Source


object Reader {
  final case class Initialize(textToRead: String)
}

class Reader extends Actor {
  val system = ActorSystem("Main")
  var router: ActorRef = system.actorOf(Props[Router], "Routeur")

  def receive: PartialFunction[Any, Unit] = {
    case Reader.Initialize(textToRead) =>
      SendToRouteEachLineOfFile(textToRead)
      takeAndPrintResultFromCounter()
      closeApplication
  }

  private def closeApplication = {
    println("Application has terminated, shutting down system")
    context.system.terminate()
    scala.sys.exit()
  }

  private def takeAndPrintResultFromCounter(): Unit = {
    implicit val timeout: Timeout = 10 seconds
    val future = router ? Router.GetFullMap
    println("Result : " + Await.result(future, timeout.duration).asInstanceOf[mutable.Map[String, Int]])
  }

  private def SendToRouteEachLineOfFile(textToRead: String): Unit = {
    val source = Source.fromFile(textToRead)
    for (line <- source.getLines()) {
      router ! Router.RouteLine(line)
    }
    source.close
  }
}

