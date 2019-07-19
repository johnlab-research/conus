package org.carbonateresearch.diagenesims.clumpedThermalModels

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

final case class SimulatedClumpedSampleOld(sample: ClumpedSample, thermalHistory: ThermalHistorySimulationOld ) {
  val name: String = sample.name
  val age: Double = sample.depositionalAge
  val depth: Int = math.ceil(sample.depositionalDepth).toInt
  val depthOffset: Int = thermalHistory.finalDepth-depth
  val D47observed: Double = sample.D47observed
  val depoTemp: Double = sample.depositionalTemperature
  val temperatureTensor: Map[Int,Map[Int,Double]] = thermalHistory.temperatureTensor
  val surfaceTMap: Map[Int,Double] = thermalHistory.depth2Temp(thermalHistory.initialAge)
  val initialSurfaceT = sample.depositionalTemperature //surfaceTMap(0)

  var simSteps = ListBuffer(ThermalCalculationStepOld(stepNumber = 0, currentDepth = depth, thermalHistory.initialAge, ClumpedEquations.davies19_D47(depoTemp), initialSurfaceT, this))

  calculate

  def getClosest[A: Numeric](num: A, list: List[A]): A = {
    val im = implicitly[Numeric[A]]
    list match {
      case x :: xs => list.minBy (y => im.abs(im.minus(y, num)))
      case Nil => throw new RuntimeException("Empty list")
    }
  }

  def depthForStep(age: Double): Int = thermalHistory.age2Depth(age)
  def ageStep: Double = thermalHistory.adjustedAgeStep

  def temperatureAtAgeDepth(stepNumber: Int, depth:Int): Double = {

    val ageDepthRel = temperatureTensor(stepNumber)

    depth>=0 match {
      case true => ageDepthRel(depth)
      case false => {val (surfAge, surfTemp) = thermalHistory.surfaceTemp.unzip
                thermalHistory.interpolator(age, surfAge, surfTemp)}
    }
  }



  def calculate = {

    @tailrec
    def calculateLoop: Unit = {
      (thermalHistory.nbSteps - simSteps.last.stepNumber) match {
        case 1 => simSteps += ThermalCalculationStepOld(simSteps.last, this)
        case _ => {
          simSteps += ThermalCalculationStepOld(simSteps.last, this)
          calculateLoop
        }
      }

    }
      calculateLoop


  }


}
