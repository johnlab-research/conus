package org.carbonateresearch.conus.dispatchers

import akka.actor.Actor

abstract class ParrallelSingleModelRunnerActor extends Actor {

  /*
  def receive = {
    case model: OldChainableCalculation => {
      val result:OldSingleModelWithResults = model.evaluate(0)
      sender ! result
    }
    case _       => println("Sample type not handled by Runner")
  }
  */
}
