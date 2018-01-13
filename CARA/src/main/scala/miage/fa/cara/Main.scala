package miage.fa.cara

import akka.actor.ActorSystem
import akka.actor.Props

object Main{

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Main")
    val reader = system.actorOf(Props[Reader], "Reader")
    reader ! Reader.Initialize("text.txt")
  }

}