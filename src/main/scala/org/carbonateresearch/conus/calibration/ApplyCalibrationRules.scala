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

package org.carbonateresearch.conus.calibration

import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.Grid

case class ApplyCalibrationRules(grid: Grid, calibrators: List[Calibrator]) {
  def results:Boolean = {
   val testList = for {
     c <- calibrators
   } yield(testSingleCalibrator(c))

    testList.foldLeft(true)(_ & _)
  }

  private def testSingleCalibrator(calibrator: Calibrator): Boolean = {
    val coordinates:List[Seq[Int]] = if(calibrator.coordinates.nonEmpty) {calibrator.coordinates} else {grid.allGridCells}
    val modelVariable:CalculationParametersIOLabels = calibrator.label

    val testList:List[Boolean] = for{
      c <- coordinates
    } yield ({
      val lastStep = grid.nbSteps-1
      val testValue:Any = grid.getVariableAtCellForTimeStep(modelVariable,c)(lastStep)
    calibrator.checkCalibration(testValue)})
    testList.foldLeft(true)(_ & _)
  }
}
