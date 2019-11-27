package org.carbonateresearch.conus.common
import spire.math.Number
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

final case class StepsCalculationResults(currentResults: Map[Number, Map[CalculationParametersIOLabels,Number]]){}
