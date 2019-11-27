package org.carbonateresearch.conus.common

import akka.actor.{Actor, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.global
import scala.util.Try
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ParrallelModellerCollectorActor extends Actor {
  var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelResults]

  override def receive = {

    case modelsList: List[ChainableCalculation] => {
      val initialCount = modelsList.size
      println("Now dispatching a modelsList of size " + modelsList.size.toString)

      implicit val timeout = Timeout(5 seconds)

      val future:List[Future[Any]] = modelsList.map(m =>
        context.actorOf(Props(new ParrallelModellerRunnerActor)) ? m )

      implicit val ec = global

      future.foreach(f =>
      f onComplete {
        case Success(model) => {
          model match {
            case m:SingleModelResults => {
          resultsList += m
          println(resultsList)}
            }}
      })

      sender ! future

      println("Dispatcher should have the results")

    }
    case s:String       => {
      println(s)
      sender ! List("This is a response from the Collector", "Did you receive it?")
    }
    case _       => println("Sample type not handled by Modeler")
  }
}
