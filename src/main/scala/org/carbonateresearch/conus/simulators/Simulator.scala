package org.carbonateresearch.conus.simulators

import org.carbonateresearch.conus.common.{ModelCalculationSpace, ModelResults, SingleModelResults}

trait Simulator {

  def save(key:ModelCalculationSpace): Unit = {
    AkkaCentralSimulatorActor.save(key)
  }

  def updateModelList(key:ModelCalculationSpace,models: List[SingleModelResults]): Unit = {
    AkkaCentralSimulatorActor.updateModelList(key,models)
  }

  def setUserDirectory(proposedPath:String): Unit = {
    AkkaCentralSimulatorActor.setUserDirectory(proposedPath)}

  def apply(key:ModelCalculationSpace):ModelResults = AkkaCentralSimulatorActor.getResults(key)


  def getResults(key:ModelCalculationSpace):ModelResults = {
    AkkaCentralSimulatorActor.getResults(key)
  }
}
