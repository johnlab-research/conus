package org.carbonateresearch.conus.util

import org.carbonateresearch.test.CalculationParametersIOLabelsT
import org.carbonateresearch.conus.common.Step

case object StepFunctionUtils{
  type StepFunction[T] =  Step => T
  type StepResults = Map[CalculationParametersIOLabelsT,Any]
  type SimulationResults = Map[Step, StepResults]
}
