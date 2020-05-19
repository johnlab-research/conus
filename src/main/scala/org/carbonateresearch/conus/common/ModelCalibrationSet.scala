package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.util.Interval

final case class ModelCalibrationSet[T](override val calibrationParameters:ModelVariable[T], override val interval: Interval[T]) extends Calibrator {

  object ModelCalibrationSet {
    def apply(parameter: ModelVariable[T], valA: T, valB: T): ModelCalibrationSet[T] = {
      new ModelCalibrationSet(parameter, Interval(valA, valB))
    }
  }

}
