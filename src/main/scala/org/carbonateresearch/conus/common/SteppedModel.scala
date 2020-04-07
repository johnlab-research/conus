package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.equations.{Calculator, OldInitializeValues, Initializer}
import org.carbonateresearch.conus.oldies.{OldChainableCalculation, OldModelCalculationSpace}


class SteppedModel(nbSteps:Int)  {

  val prepareSteps:  List[Int] = (0 to nbSteps).toList

  def next(parameter: Calculator): OldModelCalculationSpace = OldModelCalculationSpace(List(OldChainableCalculation(1,prepareSteps, List(parameter))),List())

  def defineInitialModelConditions(initializer: OldInitializeValues): OldModelCalculationSpace = {

    val initialValues = initializer.ModelCalculationSpace
    val modelsList:List[OldChainableCalculation] = initialValues.map(x => OldChainableCalculation(initialValues.indexOf(x)+1, prepareSteps, List(Initializer(x))))

    OldModelCalculationSpace(modelsList,List())}

}
