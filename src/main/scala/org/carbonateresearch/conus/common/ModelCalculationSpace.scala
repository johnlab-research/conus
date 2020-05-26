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
    val updatedModels:List[SingleModel] = models.map(m => m.copy(m.ID,m.nbSteps,m.grid,
      m.calculations,
      m.initialConditions,
      set))

    this.copy(models=updatedModels,calibrationSets=set)
  }

  def defineCalibration(set:Calibrator*) : ModelCalculationSpace = {
    this.defineCalibration(set.toList)
  }

  def size : Int = models.size

  def run: ModelCalculationSpace = {
    println("----------------------------------------"+EOL+"RUN STARTED"+EOL+"----------------------------------------")

     implicit val ec = global
      val dispatcher = new CalculationDispatcherWithFuture
      val newResults:Future[List[SingleModelResults]] = dispatcher.calculateModelsList(models)

      newResults.onComplete{
        case Success(results) => {
        this.results = results
        println(EOL+"----------------------------------------"+ EOL + "END OF RUN"+EOL+"----------------------------------------")
         ExcelEncoder.writeExcel(results,modelFolder)
      }
        case Failure(failure) => {
          println("Model has failed to run: "+failure.getMessage)}
      }

    this
  }

  def handleResults(modelResults: List[SingleModelResults]): ModelCalculationSpace = {
    ModelCalculationSpace(models, modelName,calibrationSets, modelResults)
  }

  def calibrated():List[SingleModelResults] = {
    lazy val calibratedModelList:List[SingleModelResults] = if (calibrationSets.isEmpty) {
      resultsList
    } else {resultsList.filter(p => checkAllConditions(p))}

    def checkAllConditions(thisModel: SingleModelResults):Boolean = {
      val conditions:List[Boolean] = this.calibrationSets.map(cs => {
        //cs.interval.contains(thisModel.finalResult(cs.calibrationParameters))
        false
      })
      !conditions.contains(false)
    }

    println("Found " + calibratedModelList.size + " calibrated models:")
    calibratedModelList.map(r => r.summary + EOL).foreach(println)
    print("")

    calibratedModelList
  }

}


