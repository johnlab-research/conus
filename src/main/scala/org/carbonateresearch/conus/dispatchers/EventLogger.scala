package org.carbonateresearch.conus.dispatchers

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object EventLogger extends LoggerType {

  def apply(): Behavior[runBehaviour] = Behaviors.setup { context =>

    Behaviors.receive { (context, message) => {
      message match {
        case AlmondPrintableMessage(theMessage:String, kernel: almond.api.JupyterApi, cellID:String,logger:ActorRef[runBehaviour]) => {
                kernel.publish.updateHtml (theMessage, cellID)
                Behaviors.same}
        case ConsolePrintableMessage(theMessage:String,logger:ActorRef[runBehaviour]) => {
          println(theMessage)
          Behaviors.same}
        case _ => {Behaviors.stopped}
               }
    }
    }
  }
}
