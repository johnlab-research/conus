package org.carbonateresearch.diagenesims.common

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor._
import akka.pattern.ask

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import akka.util._
import akka.util.Timeout
import org.carbonateresearch.diagenesims.DiageneSim

import scala.concurrent.duration._
import org.carbonateresearch.diagenesims.calculationparameters.CalculationResults

import scala.concurrent.ExecutionContext.global

class ParrallelModellerDispatcherActor extends Actor {
  var initialCount:Int = 0
  var resultsList = scala.collection.mutable.ListBuffer.empty[CalculationResults]
  val collector:ActorRef = context.actorOf(Props[ParrallelModellerCollectorActor], name="Collector")

  override def receive = {
    case modelsList: List[ChainableCalculation] => {
      val initialCount = modelsList.size
      val t0 = System.nanoTime()
      println("Initiating a run on " + modelsList.size.toString+ " models.")

      implicit val timeout = Timeout(30 minutes)

      val future:List[Future[Any]] = modelsList.map(m =>
        context.actorOf(Props(new ParrallelModellerRunnerActor)) ? m )

      implicit val ec = global

      future.foreach(f =>
        f onComplete {
          case Success(model) => {
            model match {
              case m:CalculationResults => {
                resultsList += m
                println(m)

                val t1 = System.nanoTime()
                val elapsedTime = ((t1 - t0)/10E9)
                val percentCompleted = ((resultsList.size.toDouble/initialCount.toDouble)*100).ceil
                val predictedTime = elapsedTime/percentCompleted*(100-percentCompleted)

                if(resultsList.size == initialCount){
                  println("-> 100% completed in "+elapsedTime+" seconds.")
                  DiageneSim.handleResults(resultsList.toList)
                }
                else {
                  println("-> "+percentCompleted+"% completed in "+elapsedTime+" seconds. Predicted time remaining: "+predictedTime+" seconds.")
                }
                }
            }}
        })

    }
    case _       => println("Sample type not handled by Modeler")
  }
}
