package miage.fa.cara

import akka.actor.Actor

import scala.collection.mutable

object Counter {
  final case class ManageRows(row: String)
  case object GetOccurrences

}

class Counter extends Actor {
  val instancesByWord: mutable.Map[String, Int] = collection.mutable.Map[String, Int]().withDefaultValue(0)

  def receive: PartialFunction[Any, Unit] = {
    case Counter.ManageRows(row) =>
      row.split(" ").foreach(word => addWordToMap(word))
    case Counter.GetOccurrences =>
      sender ! instancesByWord
  }
  private def addWordToMap(word: String): Unit = {
    instancesByWord(word.toLowerCase()) += 1
  }
}