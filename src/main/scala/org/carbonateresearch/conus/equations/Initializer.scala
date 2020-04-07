package org.carbonateresearch.conus.equations

import org.carbonateresearch.conus.equations.parametersIO._
import org.carbonateresearch.conus.common.SingleStepResults
import org.carbonateresearch.conus.oldies.OldModelResults

final case class Initializer(inputsList: List[(CalculationParametersIOLabels,Double)]) extends Calculator {

  override def outputs:List[CalculationParametersIOLabels] = inputsList.map(i => i._1)

  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {

    val newSingleStepResults: SingleStepResults = SingleStepResults(previousResults.theseResults(step).valuesForAllLabels++inputsList.toMap)
    OldModelResults(previousResults.theseResults++Map(step -> newSingleStepResults))
  }

}




