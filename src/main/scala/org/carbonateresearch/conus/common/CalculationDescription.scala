package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.Grid

case class CalculationDescription[T](f:Step=>T, label:ModelVariable[T]) extends Calculator {
  override def outputs: CalculationParametersIOLabels = label.asInstanceOf[CalculationParametersIOLabels]

  override def calculate(step: Step): Step = {
    val currentStepNumber:Int = step.stepNumber
    val theGrid:Grid = step.grid
    val coordinates:Seq[Int] = step.coordinates
    val stepError = step.stepErrors
    val calculatedValue:T = f(step)
    theGrid.setAtCell(label,calculatedValue,coordinates)(currentStepNumber)
    step
  }
}
