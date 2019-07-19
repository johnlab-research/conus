package org.carbonateresearch.diagenesims.ageSteppedModels

import org.carbonateresearch.diagenesims.calculationparameters.CalculationParameters

abstract trait AbstractAgeSteppedModelServices {
  import scala.collection.immutable.List

  def interpolator (value: Double, xs: List[Double], ys: List[Double]): Double = ???
  def calculateStep (parameters: CalculationParameters, previousStep: AbstractCalculationStep): AbstractCalculationStep = ???

}
