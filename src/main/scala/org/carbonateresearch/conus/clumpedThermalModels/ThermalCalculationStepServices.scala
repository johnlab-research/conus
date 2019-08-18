package org.carbonateresearch.conus.clumpedThermalModels

import org.carbonateresearch.conus.common.AbstractCalculationStep

trait ThermalCalculationStepServices extends AbstractCalculationStep {
   def calculate (stepNumber: Int,
                          age: Double,
                          depth: Double,
                          D47Function: (Double, Double, Double) => Double,
                          D47iStart: Double,
                          D47eq: Double,
                          dT: Double ): AbstractCalculationStep = {
    ThermalCalculationStep(stepNumber, depth, age,  D47Function(D47iStart, D47eq,dT), D47Function(D47iStart, D47eq,dT))
  }

}
