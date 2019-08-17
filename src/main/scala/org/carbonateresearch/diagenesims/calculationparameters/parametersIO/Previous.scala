package org.carbonateresearch.diagenesims.calculationparameters.parametersIO

final case class Previous(input:CalculationParametersIOLabels, offset:Int, rule:PreviousZeroHandle) extends CalculationParametersIOLabels
object Previous {
  def apply(input:CalculationParametersIOLabels) = new Previous(input, 1, TakeStepZeroValue)
  def apply(input:CalculationParametersIOLabels, offset:Int) = new Previous(input, offset, TakeStepZeroValue)
  def apply(input:CalculationParametersIOLabels, rule:PreviousZeroHandle) = new Previous(input, 1, TakeStepZeroValue)
}
