package org.carbonateresearch.conus.common

import scala.collection.immutable.List
import scala.annotation.{tailrec}

trait ForwardModelServices extends AbstractAgeSteppedModelServices {

  def getClosest(num: Double, list: List[Double]): Double = {

    list match {
      case x :: xs => list.minBy (y => math.abs(y-num))
      case Nil => throw new RuntimeException("Empty list in getClosest[] method")
    }
  }

  override def interpolator (value: Double, xs: List[Double], ys: List[Double]): Double = {
    // This method is meant to be generic and return an interpolated value between two vectors
    val topValue = getClosest(value,xs)
    val Index = xs.indexOf(topValue)
    var topX = 0.0
    var topY = 0.0
    var botX = 0.0
    var botY = 0.0

    (Index < xs.size-1) match {
      case true => {
        topY = ys(Index)
        topX = topValue
        botY = ys(Index+1)
        botX = xs(Index+1)}
      case false => {
        topY = ys(Index-1)
        topX = xs(Index-1)
        botY = ys(Index)
        botX = xs(Index)
      }
    }
    (botY-topY)/(topX-botX)*(topX-value)+topY
  }

   def interpolate (xValue: Double, pairedValues: List[(Double,Double)]): Double = {
    // This method is meant to be generic and return an interpolated value between two vectors

     val xs = pairedValues.sorted.map(a => (a)_1)
     val ys = pairedValues.sorted.map(a => (a)_2)

    val leftValue = getClosest(xValue,xs)
    val Index = xs.indexOf(leftValue)
    var topX = 0.0
    var topY = 0.0
    var botX = 0.0
    var botY = 0.0

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

  def addValueToAgeSteps (ageSteps: List[(Int,Double)], pairedValues: List[(Double,Double)]): List[(Int,Double,Double)] = {

  ageSteps.map(x => ((x)_1,(x)_2,interpolate((x)_2,pairedValues)))

  }

  def createAgeSteps (startingAge: Double, endAge: Double, numberOfSteps: Int): List[(Int,Double)] ={
    val ageStep = math.abs(startingAge-endAge)/(numberOfSteps)
    val stepList = (0 to numberOfSteps).toList

    stepList.map(stepNb => (stepNb, (numberOfSteps-stepNb)*ageStep))
  }

  def ageAndDepthFromBurialHistory (burialHistory: List[(Double,Double)], numberOfSteps: Int): List[(Int, Double, Double)] ={
    val startAge = burialHistory.map(a => (a)_1).max
    val endAge = burialHistory.map(a => (a)_1).min

    addValueToAgeSteps(createAgeSteps(startAge, endAge, numberOfSteps), burialHistory)
  }

}

