package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels, NumberOfSteps, StandardsParameters}
import org.carbonateresearch.conus.common.ModelResults

final case class CalculateStepAges(maxAge: Double, minAge:Double) extends Calculator with StandardsParameters{

  override val outputs = List(Age)

   override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {
     val myNbSteps:Double = previousResults.getStepResult(step,NumberOfSteps)
     val increment = (maxAge-minAge)/(myNbSteps-1)
    MultiplierFromStepsCP(output = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)
  }

}
