package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

case class MultiStepsResults(results: Map[Int, SingleStepResults]){
  val steps = results.keys
}
