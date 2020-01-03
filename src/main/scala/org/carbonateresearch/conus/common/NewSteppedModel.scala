package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.{CalculationStepValue, InitializeValues, Initializer}


final case class NewSteppedModel(nbSteps:Int)  {

  val prepareSteps:  List[Int] = (0 to nbSteps).toList

  def next(parameter: CalculationStepValue): ModelCalculationSpace = ModelCalculationSpace(List(ChainableCalculation(1,prepareSteps, List(parameter))),Map())


  def next(initializer: InitializeValues): ModelCalculationSpace = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[ChainableCalculation] = initialValues.map(x => ChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    ModelCalculationSpace(modelsList,Map())}

}
