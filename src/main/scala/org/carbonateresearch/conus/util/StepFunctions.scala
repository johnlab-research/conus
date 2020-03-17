package org.carbonateresearch.conus.util

import org.carbonateresearch.test.CalculationParametersIOLabelsT

case object StepFunctions{
  type Step = Int
  type StepFunction[T] =  Step => T
  type StepResults = Map[CalculationParametersIOLabelsT,Any]
  type SimulationResults = Map[Step, StepResults]
}
