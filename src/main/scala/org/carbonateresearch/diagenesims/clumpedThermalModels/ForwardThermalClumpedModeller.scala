package org.carbonateresearch.diagenesims.clumpedThermalModels
import akka.actor.Actor

class ForwardThermalClumpedModeller extends Actor with ThermalForwardModelServices {
  def receive = {
    case sampleList: List[ClumpedSample] => sampleList.map(x => x.name).foreach(println)
    case _       => println("Sample type not handled")
  }
}
