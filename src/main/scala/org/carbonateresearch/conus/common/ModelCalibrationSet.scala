package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
import org.carbonateresearch.conus.util.Interval

final case class ModelCalibrationSet(calibrationParameters:CalculationParametersIOLabels, interval: Interval)

object ModelCalibrationSet {
  def apply(parameter:CalculationParametersIOLabels, valA: Double, valB: Double): ModelCalibrationSet ={

    val (myMin, myMax) = if(valA <= valB) {(valA, valB)} else {(valB,valA)}
    new ModelCalibrationSet(parameter,Interval(myMin, myMax))
    }
  }
