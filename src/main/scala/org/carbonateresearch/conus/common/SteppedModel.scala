package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid, GridFactory}

class SteppedModel(nbSteps:Int, gridGeometry:Seq[Int]=Seq(1),calculations:List[ChainableCalculation]=List())  {

  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  //def next(parameter: Calculator): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),List())
  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,gridDimensions)
  }

  def defineInitialModelConditions(initializer: InitializeValues): SteppedModel = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    new SteppedModel(nbSteps,gridGeometry,modelsList)}

  def defineMathematicalModelPerCell(calculationList: Calculator*): ModelCalculationSpace = {
    val newCalculators:List[Calculator] = calculationList.toList
    val newChainableCalculations: List[ChainableCalculation] = calculations.map(cl =>
      ChainableCalculation(cl.ID, cl.steps, (cl.modelParameters++newCalculators).reverse))
    val allModelVariables:List[Calculator] = calculations.head.modelParameters
    val dataIndex:Map[CalculationParametersIOLabels,Int] = allModelVariables.map(c => (c.outputs,allModelVariables.indexOf(c))).toMap

    ModelCalculationSpace(GridFactory(gridGeometry,nbSteps,dataIndex),newChainableCalculations, List())
  }
}
