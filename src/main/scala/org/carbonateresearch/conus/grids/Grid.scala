package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.CalculationParametersIOLabels

trait Grid {
  type TimeStep = Int
  type Dimensions = Seq[Int]
  def gridGeometry:Dimensions
  def size:Int = gridGeometry.product
  def variableMap:Map[CalculationParametersIOLabels,Int]
  def nbSteps: Int
  def numberOfCells:Int = gridGeometry.product * nbSteps

  override def toString: String = {
    "Grid with "+numberOfCells+" cells in "+gridGeometry.size+" dimensions"
  }
  def printGridResults:String
  def summary:String
  def getVariableAtCellForTimeStep[T](key:CalculationParametersIOLabels, coordinates:Dimensions)(implicit timeStep: TimeStep):T
  def getVariableForTimeStep(key:CalculationParametersIOLabels)(implicit timeStep: TimeStep):GridElement
  def getTimeStep(timeStep:TimeStep):GridElement
  def setAtCell[T](key:CalculationParametersIOLabels,value:T, coordinates:Dimensions)(implicit timeStep: TimeStep):Unit
  def setManyAtCell(keys:Seq[CalculationParametersIOLabels],values:Seq[Any], coordinates:Dimensions)(implicit timeStep: TimeStep):Unit
  def setManyForTimeStep(keys:Seq[CalculationParametersIOLabels],value:Seq[Any])(implicit timeStep: TimeStep):Unit
  def initializeGrid(keys:Seq[CalculationParametersIOLabels],values:Seq[Any]):Unit
}
