package org.carbonateresearch.diagenesims.thermalmodel

class CalculationStep (val stepNumber: Int, val myDepth: Int, val myAge: Double, val D47iStart: Double, val temp: Double, val sample: SimulatedSample) {
  var D47iFinal: Double = D47iStart
  var tempFinal: Double = temp
  val thermalRegime: Map[Int, Double] = sample.temperatureTensor(stepNumber)

  if (stepNumber != 0) D47iFinal = calculate

  //println(sample.name, "| #"+stepNumber+"| Age = "+f"$myAge%1.1f"+" Ma | Depth = "+myDepth+" m | Temp = "+f"$temp%1.1f"+"˚C | Δ47obs = "+f"$D47iFinal%1.4f"+"%")

  def age: Double = myAge
  def finalTemp: Double = math.sqrt(0.04028 * math.pow(10,6)/(calculate - 0.23776))-273.15
  def tempKelvin: Double = temp + 273.15

  def D47eq: Double = {
    0.04028 * math.pow(10,6) / math.pow(tempKelvin,2) + 0.23776
  }


  def calculate: Double = {
    val Tref = Constants.tref
    val Kref = Constants.kref
    val Ea = Constants.ea
    val R = Constants.r
    val dT = sample.ageStep * 1000000 * 365 * 24 * 60 * 60

    (D47iStart - D47eq) * math.exp(-dT * Kref * math.exp(Ea / R * ((1 / Tref) - (1 / tempKelvin)))) + D47eq
  }
}

object CalculationStep {
  def apply(stepNumber: Int, currentDepth: Int, prevStepAge: Double, prevD47i: Double, prevTemp: Double, sample: SimulatedSample): CalculationStep = {
    new CalculationStep(stepNumber, currentDepth, prevStepAge, prevD47i, prevTemp, sample)}


  def apply(previousStep: CalculationStep, sample: SimulatedSample): CalculationStep = {

    val newStepNumber = previousStep.stepNumber+1
    val newAgeOfStep = previousStep.age - sample.ageStep
    val depthOffset = sample.depthForStep(sample.age)
    val newDepth = if (newAgeOfStep <= sample.age) sample.depthForStep(newAgeOfStep)-depthOffset + sample.depth else 0
    val newTemp = sample.temperatureAtAgeDepth(newStepNumber, newDepth)

    new CalculationStep(newStepNumber,
      newDepth,
      newAgeOfStep,
      previousStep.D47iFinal,
      newTemp,
      sample)
  }
}
