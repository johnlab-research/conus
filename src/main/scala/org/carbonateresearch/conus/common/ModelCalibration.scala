package org.carbonateresearch.conus.common
  import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels
  import spire.math.{Interval, Number}

final case class ModelCalibration (calibrationParameters:(CalculationParametersIOLabels,Interval[Number]))
