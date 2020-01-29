package org.carbonateresearch.conus.common

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class ParrallelSingleModelRunnerActor extends Actor {

  def receive = {
    case model: ChainableCalculation => {
      val result:SingleModelWithResults = model.evaluate(0)
      sender ! result
    }
    case _       => println("Sample type not handled by Runner")
  }
}
