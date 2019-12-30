package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps, StandardsParameters}
import spire.math.Number
import spire.implicits._

final case class CalulateStepAges(maxAge: Number, minAge:Number) extends CalculationStepValue with StandardsParameters{
 override val outputs = List(Age)
   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
     val increment = (maxAge-minAge)/(previousResults(step)(NumberOfSteps)-1)
    MultiplierFromStepsCP(output = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)

  }

}
