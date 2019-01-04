package thermalmodel

import scala.collection.mutable.ListBuffer


class SimulatedSample(sample: Sample, thermalHistory: ThermalHistorySimulation ) {
  val name: String = sample.name
  val age: Double = sample.getSampleAge
  val depth: Int = math.ceil(sample.getDepth).toInt
  val depthOffset: Int = thermalHistory.finalDepth-depth
  val D47observed: Double = sample.getD47Observed
  val depoTemp: Double = sample.getDepositionalTemperature
  val temperatureTensor: Map[Int,Map[Int,Double]] = thermalHistory.temperatureTensor
  val surfaceTMap: Map[Int,Double] = thermalHistory.depth2Temp(thermalHistory.initialAge)
  val initialSurfaceT = surfaceTMap(0)


  var simSteps = ListBuffer(CalculationStep(stepNumber = 0, currentDepth = 0, thermalHistory.initialAge, D47observed, initialSurfaceT, this))

  calculate


  def depthForStep(age: Double): Int = thermalHistory.age2Depth(age)
  def ageStep: Double = thermalHistory.adjustedAgeStep

  def temperatureAtAgeDepth(stepNumber: Int, depth:Int): Double = {
    val ageDepthRel = temperatureTensor(stepNumber)
    if(depth>=0){ageDepthRel(depth)}
    else {
      val (surfAge, surfTemp) = thermalHistory.surfaceTemp.unzip
      thermalHistory.interpolator(age, surfAge, surfTemp)
    }
  }

  def calculate = {
    while(simSteps.last.stepNumber != thermalHistory.nbSteps){
      simSteps+=CalculationStep(simSteps.last, this)
    }
  }

}
