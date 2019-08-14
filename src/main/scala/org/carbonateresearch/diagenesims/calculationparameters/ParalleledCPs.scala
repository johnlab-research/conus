package org.carbonateresearch.diagenesims.calculationparameters

import spire.math.Number
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.carbonateresearch.diagenesims.common.ParrallelModellerDispatcherActor
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO.{CalculationParametersIOLabels}

final case class ParalleledCPs(parametersList:Vector[CalculationParameters]) extends CalculationParameters {

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
     val actorSystem = ActorSystem("Diagenesim-Akka")

     val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])

     val results = parametersList.map(_.calculate(step, previousResults))
     //val results = parametersList.map(m => modeller ! m))

     results.foldLeft(previousResults)(_++_)

  }
}
