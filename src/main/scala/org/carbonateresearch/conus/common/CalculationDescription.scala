/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.Grid

case class CalculationDescription[T](f:Step=>T, label:ModelVariable[T]) extends Calculator {
  override def outputs: CalculationParametersIOLabels = label.asInstanceOf[CalculationParametersIOLabels]

  override def calculate(step: Step): Step = {
    val currentStepNumber:Int = step.stepNumber
    val theGrid:Grid = step.grid
    val coordinates:Seq[Int] = step.coordinates
    val calculatedValue:T = f(step)
    theGrid.setAtCell(label,calculatedValue,coordinates)(currentStepNumber)
    step
  }
}
