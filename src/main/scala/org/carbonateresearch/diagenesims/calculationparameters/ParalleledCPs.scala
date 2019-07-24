package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.Number
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.carbonateresearch.diagenesims.common.ParrallelModellerDispatcherActor

final case class ParalleledCPs(parametersList:Vector[CalculationParameters]) extends CalculationParameters {

   override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]]  = {
     val actorSystem = ActorSystem("Diagenesim-Akka")

     val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])

     val results = parametersList.map(_.calculate(steps, previousResults))
     //val results = parametersList.map(m => modeller ! m))

     results.foldLeft(previousResults)(_++_)

  }
}
