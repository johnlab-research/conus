package org.carbonateresearch.conus.util

import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.common.Step

case object StepFunctionUtils{
  type StepFunction[T] =  Step => T
  type StepResults = Map[CalculationParametersIOLabels,Any]
  type SimulationResults = Map[Step, StepResults]
}
