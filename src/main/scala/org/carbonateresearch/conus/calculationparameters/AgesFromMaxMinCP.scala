package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{Age, CalculationParametersIOLabels, NumberOfSteps}
import spire.math.{Number}
import spire.implicits._

final case class AgesFromMaxMinCP(maxAge: Number, minAge:Number) extends CalculationParameters{

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
     val increment = (maxAge-minAge)/(previousResults(step)(NumberOfSteps)-1)
    MultiplierFromStepsCP(outputValueLabel = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)

  }

}
