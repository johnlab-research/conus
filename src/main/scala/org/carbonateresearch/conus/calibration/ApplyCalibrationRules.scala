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
import scala.util.{Success, Try}
import math.pow

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
    val testStep = if(calibrator.step.isEmpty){grid.nbSteps-1}else(calibrator.step.get)

    val testList:List[Boolean] = if(testStep>grid.nbSteps-1){List(false)} else {for{
      c <- coordinates
    } yield ({
      val testValue:Any = grid.getVariableAtCellForTimeStep(modelVariable,c)(testStep)
    calibrator.checkCalibration(testValue)})}

    testList.foldLeft(true)(_ & _)
  }

  def rsme:Option[Double] = {
    val allRSMEs = calibrators.flatMap(calibrator => {
    val coordinates:List[Seq[Int]] = if(calibrator.coordinates.nonEmpty) {calibrator.coordinates} else {grid.allGridCells}
    val modelVariable:CalculationParametersIOLabels = calibrator.label
    val testStep = if(calibrator.step.isEmpty || calibrator.step.get > grid.nbSteps-1){grid.nbSteps-1}else(calibrator.step.get)

      for{
        c <- coordinates
      } yield ({
        val testValue:Any = grid.getVariableAtCellForTimeStep(modelVariable,c)(testStep)
        calibrator.squaredError(testValue)})})

    val validRSMEs = allRSMEs.filter{p => p match {
                                          case p:Some[Double] => true
                                          case None => false}}
    val attempt = Try {
    val N = validRSMEs.length.toDouble
    val sumOfSquares:Double = {for(rsme <- validRSMEs)yield(rsme.get)}.sum
    pow(sumOfSquares/N,0.5)}

    attempt match {
      case p:Success[Double] => Some(p.value)
      case _ => None
    }

  }
}
