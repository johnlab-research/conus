package org.carbonateresearch.conus.util

import org.carbonateresearch.conus.common.CalculationParametersIOLabels

sealed trait PreviousZeroHandle
case object TakeStepZeroValue extends PreviousZeroHandle
case object TakeCurrentStepValue extends PreviousZeroHandle
case class TakeSpecificValue(value:Double) extends PreviousZeroHandle
case class TakeValueForLabel(label:CalculationParametersIOLabels) extends PreviousZeroHandle

