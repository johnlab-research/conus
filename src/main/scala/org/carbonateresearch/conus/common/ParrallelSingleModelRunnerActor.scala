package org.carbonateresearch.conus.common

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.carbonateresearch.conus.oldies.{OldChainableCalculation, OldSingleModelWithResults}

class ParrallelSingleModelRunnerActor extends Actor {

  def receive = {
    case model: OldChainableCalculation => {
      val result:OldSingleModelWithResults = model.evaluate(0)
      sender ! result
    }
    case _       => println("Sample type not handled by Runner")
  }
}
