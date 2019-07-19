package org.carbonateresearch.diagenesims.calculationparameters

import spire.math._
import spire.implicits._
import spire.algebra._

final case class InterpolatorCP(outputValueLabel: String, inputValueLabel:String, xyList: List[(Number, Number)])
extends CalculationParameters {

  override def calculate (steps:List[Number],previousResults:Map[String,Map[Number,Number]]): Map[String, Map[Number,Number]] = {

    val xValues = previousResults(inputValueLabel)

    Map(outputValueLabel -> xValues.map(x => (x._1, interpolateSingleValue(x._2, xyList))))

  }

  private[InterpolatorCP] def getClosest (num: Number, list: List[Number]): Number = {
    implicit val ord = Ordering.by { foo: Number => foo.toDouble}

      list match {
      case x :: xs => list.minBy(y => abs(y-num))
      case Nil => throw new RuntimeException("Empty list in getClosest[] method")
    }
  }

  private[InterpolatorCP] def interpolateSingleValue(xValue: Number, pairedValues: List[(Number,Number)]): Number = {
    // This method is meant to be generic and return an interpolated value between two vectors
    //implicit def convertToDoubleHere(x: T): Double = x.toDouble
    //implicit def convertListToDoubleHere(xs: List[T]): List[Double] = xs.map(x => x.toDouble)
     //def convertListToDoubleDoubleHere(xs: List[(T,T)]): List[(Double,Double)] = xs.map(x => (x._1.toDouble,x. _2.toDouble))
    //val xy = convertListToDoubleDoubleHere(pairedValues)
    implicit val ord = Ordering.by { foo: Number => foo.toDouble}

    val xs = pairedValues.sorted.map(a => a._1)
    val ys = pairedValues.sorted.map(a => a._2)

    val leftValue = getClosest(xValue,xs)
    val Index = xs.indexOf(leftValue)
    var topX = Number(0);
    var topY = Number(0);
    var botX = Number(0);
    var botY = Number(0);

    (Index < pairedValues.size-1) match {
      case true => {
        topY = ys(Index)
        topX = leftValue
        botY = ys(Index+1)
        botX = xs(Index+1)}
      case false => {
        topY = ys(Index-1)
        topX = xs(Index-1)
        botY = ys(Index)
        botX = xs(Index)
      }
    }
    (botY-topY)/(topX-botX)*(topX-xValue)+topY
  }

}
