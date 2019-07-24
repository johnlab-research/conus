package org.carbonateresearch.diagenesims.common

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.carbonateresearch.diagenesims.calculationparameters.CalculationResults

class ParrallelModellerRunnerActor extends Actor {

  def receive = {
    case model: ChainableCalculation => {
      val result:CalculationResults = model.evaluate
      sender ! result
    }
    case _       => println("Sample type not handled by Runner")
  }
}
