package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.util.Intervalable

trait Calibrator {
  def interval:Intervalable
  def calibrationParameters:CalculationParametersIOLabels
}
