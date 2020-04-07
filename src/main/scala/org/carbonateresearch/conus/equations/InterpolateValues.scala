package org.carbonateresearch.conus.equations
import org.carbonateresearch.conus.equations.parametersIO.CalculationParametersIOLabels
import org.carbonateresearch.conus.common.SingleStepResults
import org.carbonateresearch.conus.oldies.OldModelResults

final case class InterpolateValues(output: CalculationParametersIOLabels, inputValueLabel:CalculationParametersIOLabels, xyList: List[(Double, Double)])
extends Calculator {

  val outputs=List(output)
  override val inputs = Some(List(inputValueLabel))

  override def calculate (step:Int,previousResults:OldModelResults): OldModelResults = {

    val xValue = previousResults.getStepResult(step,inputValueLabel)
    previousResults.addParameterResultAtStep(output,interpolateSingleValue(xValue, xyList),step)

  }

  private[InterpolateValues] def interpolateSingleValue(xValue: Double, pairedValues: List[(Double,Double)]): Double = {
    // This method is meant to be generic and return an interpolated value between two numbers


    val xs = pairedValues.sorted.map(a => a._1)
    val ys = pairedValues.sorted.map(a => a._2)

    val firstNegative:Option[Double] = xs.find(x => (xValue-x)<=0)

    var Index = 0

    firstNegative match {
      case Some(i) => {
        if(xs.indexOf(i) < xs.size && xs.indexOf(i)>0){
          Index = xs.indexOf(i)
        } else if (xs.indexOf(i)==0){
          Index = 1
        } else {
          Index = xs.indexOf(i)-1
        }
      }
      case None => Index = xs.size-1 }

    val topY = ys(Index-1)
    val topX = xs(Index-1)
    val botY = ys(Index)
    val botX = xs(Index)

    (botY-topY)/(topX-botX)*(topX-xValue)+topY
  }

}
