package org.carbonateresearch.conus.equations

import org.carbonateresearch.conus.equations.parametersIO.CalculationParametersIOLabels
import org.carbonateresearch.conus.oldies.OldModelResults


final case class MultiplierFromStepsCP(output: CalculationParametersIOLabels, maxValue: Double, minValue: Double, increment: Double) extends Calculator {

  val outputs = List(output)

  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {

        val resultsList = maxValue-(step*increment)
    //ModelResults().newModelResultWithLabel(output).forValue(resultsList).atStep(step)
    previousResults.addParameterResultAtStep(output,resultsList,step)

  }

}
