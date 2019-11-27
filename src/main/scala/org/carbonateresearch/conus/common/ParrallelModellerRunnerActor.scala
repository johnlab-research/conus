package org.carbonateresearch.conus.common

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class ParrallelModellerRunnerActor extends Actor {

  def receive = {
    case model: ChainableCalculation => {
      val result:SingleModelResults = model.evaluate
      sender ! result
    }
    case _       => println("Sample type not handled by Runner")
  }
}
