package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps, StandardsParameters}
import org.carbonateresearch.conus.common.ModelResults


final case class CalulateStepAges(maxAge: Double, minAge:Double) extends CalculationStepValue with StandardsParameters{
 override val outputs = List(Age)
   override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {
     val increment = (maxAge-minAge)/(previousResults.resultsForStep(step).valueForLabel(NumberOfSteps)-1)
    MultiplierFromStepsCP(output = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)
  }

}
