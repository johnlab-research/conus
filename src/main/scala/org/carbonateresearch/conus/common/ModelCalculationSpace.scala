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


import scala.concurrent.Future
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.calibration.Calibrator
import org.carbonateresearch.conus.dispatchers.CalculationDispatcherWithFuture
import org.carbonateresearch.conus.grids.Grid

import scala.concurrent.ExecutionContext.global
import scala.util.{Failure, Success}

final case class ModelCalculationSpace(models: List[SingleModel] = List(),
                                       modelName:String,
                                       calibrationSets: List[Calibrator] = List(),
                                       var results: List[SingleModelResults] = List()) {

  var resultsList:List[SingleModelResults] = scala.collection.mutable.ListBuffer.empty[SingleModelResults].toList
  private val EOL = lineSeparator()
  private val modelFolder:String = System.getProperty("user.home")+"/Conus/"+modelName

  def defineCalibration(set:List[Calibrator]) : ModelCalculationSpace = {
    val updatedModels:List[SingleModel] = models.map(m => m.copy(m.ID,m.nbSteps,m.gridGeometry,
      m.calculations,
      m.initialConditions,
      set))

    this.copy(models=updatedModels,calibrationSets=set)
  }

  def defineCalibration(set:Calibrator*) : ModelCalculationSpace = {
    this.defineCalibration(set.toList)
  }

  def size : Int = models.size

}


