package org.carbonateresearch.conus

import org.carbonateresearch.conus.dispatchers.CalculationDispatcherAkka
import akka.actor.typed.{ActorSystem}
import org.carbonateresearch.conus.common.{ModelCalculationSpace, SingleModelResults}
import scala.collection.mutable.Map


object RunScheduler {
  val system: ActorSystem[CalculationDispatcherAkka.runBehaviour] = ActorSystem(CalculationDispatcherAkka(),"CoNuS")
  var runnedModels:Map[ModelCalculationSpace,List[SingleModelResults]] = Map()

  def run(models: ModelCalculationSpace): Unit = {
    if (runnedModels.isDefinedAt(models)) {runnedModels.remove(models)}
    system ! CalculationDispatcherAkka.RunMultipleModels(models)
  }

  def updateModelList(key:ModelCalculationSpace,models: List[SingleModelResults]): Unit = {
    runnedModels.addOne(key,models)
  }

  def apply(key:ModelCalculationSpace):List[SingleModelResults] = getResults(key)

  def getResults(key:ModelCalculationSpace):List[SingleModelResults] =
    if (runnedModels.isDefinedAt(key)) {runnedModels(key)} else {
      runnedModels(key)
    }
}
