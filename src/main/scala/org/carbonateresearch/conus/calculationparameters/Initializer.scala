package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.common.{ModelResults, SingleStepResults}

final case class Initializer(inputsList: List[(CalculationParametersIOLabels,Double)]) extends Calculator {

  override def outputs:List[CalculationParametersIOLabels] = inputsList.map(i => i._1)

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

    val newSingleStepResults: SingleStepResults = SingleStepResults(previousResults.theseResults(step).valuesForAllLabels++inputsList.toMap)
    ModelResults(previousResults.theseResults++Map(step -> newSingleStepResults))
  }

}




