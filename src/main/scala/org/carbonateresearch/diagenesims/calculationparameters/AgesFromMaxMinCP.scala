package org.carbonateresearch.diagenesims.calculationparameters
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO.{Age, CalculationParametersIOLabels, NumberOfSteps}
import spire.math.{Number, Numeric}
import spire.implicits._

final case class AgesFromMaxMinCP(maxAge: Number, minAge:Number) extends CalculationParameters{

   override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {
     val increment = (maxAge-minAge)/(previousResults(step)(NumberOfSteps)-1)
    MultiplierFromStepsCP(outputValueLabel = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)

  }

}
