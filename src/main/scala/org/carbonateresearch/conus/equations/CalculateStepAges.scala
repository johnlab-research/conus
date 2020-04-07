package org.carbonateresearch.conus.equations
import org.carbonateresearch.conus.equations.parametersIO.{CalculationParametersIOLabels, NumberOfSteps, StandardsParameters}
import org.carbonateresearch.conus.oldies.OldModelResults

final case class CalculateStepAges(maxAge: Double, minAge:Double) extends Calculator with StandardsParameters{

  override val outputs = List(Age)

   override def calculate (step:Int,previousResults:OldModelResults): OldModelResults  = {
     val myNbSteps:Double = previousResults.getStepResult(step,NumberOfSteps)
     val increment = (maxAge-minAge)/(myNbSteps-1)
    MultiplierFromStepsCP(output = Age, maxValue = maxAge, minValue = minAge, increment=increment).calculate(step, previousResults)
  }

}
