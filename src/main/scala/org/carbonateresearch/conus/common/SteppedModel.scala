package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.{Calculator, InitializeValues, Initializer}


class SteppedModel(nbSteps:Int)  {

  val prepareSteps:  List[Int] = (0 to nbSteps).toList

  def next(parameter: Calculator): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),List())

  def defineInitialModelConditions(initializer: InitializeValues): ModelCalculationSpace = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    ModelCalculationSpace(modelsList,List())}

}
