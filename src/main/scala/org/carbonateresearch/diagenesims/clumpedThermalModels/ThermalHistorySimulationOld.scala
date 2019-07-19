package org.carbonateresearch.diagenesims.clumpedThermalModels

import org.carbonateresearch.diagenesims.ageSteppedModels.ForwardModelServices


final case class ThermalHistorySimulationOld(ageStep:Double, burialHistory: List[(Double,Double)], geothermalGradient: List[(Double,Double)], surfaceTemp: List[(Double, Double)], samples: List[ClumpedSample])
extends ForwardModelServices
{
  import scala.annotation.tailrec
  val (burialAge, burialDepth) = burialHistory.unzip
  val initialAge: Double = burialAge.head
  val finalAge: Double = 0.0
  val initialDepth: Int = 0
  val finalDepth: Int = math.ceil(burialDepth.max).toInt
  val nbSteps: Int = math.ceil(initialAge/ageStep).toInt
  val adjustedAgeStep: Double =  initialAge/nbSteps
  val stepIndex: Vector[Int] = Vector.range(0, nbSteps+1, 1)
  val burialDepths: Vector[Int] = Vector.range(0,finalDepth+1,1)
  val ageMap: Map[Int, Double] = stepIndex.zip(ages).toMap
  val temperatureTensor: Map[Int,Map[Int,Double]] = tempTensorMap
  val results: List[SimulatedClumpedSampleOld] = this.calculate

  def calculate: List[SimulatedClumpedSampleOld] = samples.map(s => new SimulatedClumpedSampleOld(s, this))

  def ages: Vector[Double] = stepIndex.map(step => initialAge - (step * adjustedAgeStep))

  def depth2Temp (thisAge: Double): Map[Int, Double] = {
    val (geothAge, _ ) = geothermalGradient.unzip
    val (surfAge, surfTemp) = surfaceTemp.unzip
    val geothermIndex = getClosest(thisAge,geothAge)
    val geoGradMap = geothermalGradient.toMap
    val thisGeoGradient = geoGradMap(geothermIndex)
    val interpolatedSurfaceT = interpolator(thisAge, surfAge, surfTemp)
    val depthTemp = burialDepths.map(depth => depth * thisGeoGradient/1000 + interpolatedSurfaceT)

    burialDepths.zip(depthTemp).toMap
  }

  def tempTensorMap: Map[Int, Map[Int, Double]] = ageMap map { case (k, v) => k -> depth2Temp(v) }

  def age2Depth (simulatedAge: Double): Int = interpolator(simulatedAge, burialAge, burialDepth).toInt

}
