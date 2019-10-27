package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO.{CalculationParametersIOLabels}

import spire.math._
import spire.implicits._
import spire.algebra._

final case class InterpolatorCP(output: CalculationParametersIOLabels, inputValueLabel:CalculationParametersIOLabels, xyList: List[(Number, Number)])
extends CalculationParameters {

  val outputs=List(output)

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]] = {

    val xValue = previousResults(step)(inputValueLabel)

    Map(step -> Map(output -> interpolateSingleValue(xValue, xyList)))

  }

  private[InterpolatorCP] def interpolateSingleValue(xValue: Number, pairedValues: List[(Number,Number)]): Number = {
    // This method is meant to be generic and return an interpolated value between two numbers

    implicit val ord = Ordering.by { foo: Number => foo.toDouble}

    val xs = pairedValues.sorted.map(a => a._1)
    val ys = pairedValues.sorted.map(a => a._2)

    val firstNegative:Option[Number] = xs.find(x => (xValue-x)<=0)

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
