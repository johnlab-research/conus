package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.GridElement

trait SimulationResults {
  def resultsForStep(stepNumber: Int): GridElement
  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): GridElement
  def prettyPrint[T](k:ModelVariable[T],step:Int,coordinates:Seq[Int]):String
  def isDefinedAt(step:Int,coordinates:Seq[Int]):Boolean
  def variableIsDefinedAt[T](k:ModelVariable[T],step:Int,coordinates:Seq[Int]):Boolean
}


