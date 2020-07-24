package org.carbonateresearch.conus.dispatchers

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors


object ModelDispatcherAkka {
  def apply(): Behavior[runBehaviour] = Behaviors.setup { context =>

    Behaviors.receive { (context, message) =>
      message match {
        case RunMultipleModels(modelsToRun,pid,autoSave,loggerType) => {
          val calculationDispatcher:ActorRef[runBehaviour] = context.spawn(CalculationDispatcherAkka(), s"Process-$pid-calculationDispatcher")
          calculationDispatcher ! message
          Behaviors.same
        }
      }
    }
  }
}
