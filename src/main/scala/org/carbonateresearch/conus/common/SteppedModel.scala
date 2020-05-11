package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid, GridFactory}

class SteppedModel(nbSteps:Int, gridGeometry:Seq[Int]=Seq(1))  {

  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  //def next(parameter: Calculator): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),List())
  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,gridDimensions)
  }

  def defineMathematicalModelPerCell(calculationList: Calculator*): SteppedModelWithCalculations = {
    val mathematicalModel:List[Calculator] = calculationList.toList

    SteppedModelWithCalculations(nbSteps:Int, gridGeometry:Seq[Int],mathematicalModel:List[Calculator])
  }


}
