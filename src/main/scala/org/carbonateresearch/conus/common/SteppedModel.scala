package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.Grid

class SteppedModel(nbSteps:Int, grid:Grid = Grid(1))  {

  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  //def next(parameter: Calculator): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),List())
  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,Grid(gridDimensions.toList))
  }

  def defineInitialModelConditions(initializer: InitializeValues): ModelCalculationSpace = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    ModelCalculationSpace(grid,modelsList,List())}
}
