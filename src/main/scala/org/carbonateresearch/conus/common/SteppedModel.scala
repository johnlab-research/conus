package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.{Grid, GridFactory}

class SteppedModel(nbSteps:Int, gridGeometry:Seq[Int]=Seq(1),initialConditions:List[ChainableCalculation]=List())  {

  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  //def next(parameter: Calculator): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),List())
  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,gridDimensions,initialConditions)
  }

  def defineInitialModelConditions(initializer: InitializeValues): SteppedModel = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    new SteppedModel(nbSteps,gridGeometry,initialConditions=modelsList)}

  def defineMathematicalModelPerCell(calculationList: Calculator*): ModelCalculationSpace = {
    val newCalculators:List[Calculator] = calculationList.toList
    val newChainableCalculations: List[ChainableCalculation] = initialConditions.map(cl =>
      ChainableCalculation(cl.ID, cl.steps, (cl.modelParameters++newCalculators).reverse))
    val listOfOutputs:List[Calculator] = newChainableCalculations.head.modelParameters
    val dataIndex:Map[CalculationParametersIOLabels,Int] = listOfOutputs.map(c => (c.outputs,listOfOutputs.indexOf(c))).toMap

    ModelCalculationSpace(GridFactory(gridGeometry,nbSteps,dataIndex),newChainableCalculations, List())
  }


}
