package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
import org.carbonateresearch.conus.common.ModelResults


final case class MultiplierFromStepsCP(output: CalculationParametersIOLabels, maxValue: Double, minValue: Double, increment: Double) extends CalculationStepValue {

  val outputs = List(output)

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

        val resultsList = maxValue-(step*increment)
    //ModelResults().newModelResultWithLabel(output).forValue(resultsList).atStep(step)
    previousResults.addParameterResultAtStep(output,resultsList,step)

  }

}
