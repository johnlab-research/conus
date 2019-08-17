package org.carbonateresearch.diagenesims.calculationparameters.parametersIO
import spire.math._

sealed trait PreviousZeroHandle
case object TakeStepZeroValue extends PreviousZeroHandle
case object TakeCurrentStepValue extends PreviousZeroHandle
case class TakeSpecificValue(value:Number) extends PreviousZeroHandle

