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

import org.carbonateresearch.conus.grids.{Grid, GridFactory}
import org.carbonateresearch.conus.util.TimeUtils
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.Step
import org.carbonateresearch.conus.calibration.{ApplyCalibrationRules, Calibrator}

case class SingleModel(ID:Int,
                       nbSteps:Int,
                       gridGeometry:Seq[Int],
                       calculations:List[Calculator],
                       initialConditions:List[InitialCondition],
                       calibrationSet:List[Calibrator]=List(),
                      modelName:String = "Anonymous") extends Combinatorial {
  val EOL:String = lineSeparator()
  val steps:Seq[Int] = (0 until nbSteps).toList

  private val allGridCells:List[Seq[Int]] = {
    gridGeometry.size match {
      case 0 => List(Seq(1))
      case 1 => (0 until gridGeometry.head).map(x=>Seq(x)).toList
      case 2 => {
        val firstCoord:Seq[Int] = (0 until gridGeometry.head)
        val secondCoord:Seq[Int] = (0 until gridGeometry(1))
        for {
          f <- firstCoord
          s <- secondCoord
        } yield Seq(f,s)
      }.toList
      case 3 => {
        val firstCoord:Seq[Int] = (0 until gridGeometry.head)
        val secondCoord:Seq[Int] = (0 until gridGeometry(1))
        val thirdCoord:Seq[Int] = (0 until gridGeometry(2))
        for {
          f <- firstCoord
          s <- secondCoord
          t <- thirdCoord
        } yield Seq(f,s,t)
        }.toList
    }
  }

  def evaluate(startTime:Double): SingleModelResults = {
    val grid:Grid = createAndInitializeGrid(initialConditions)
    steps.foreach(s => {
      calculations.foreach(c => {
        allGridCells.foreach(coordinates => {
          val currentStep = Step(s,coordinates,grid,"")
          c.calculate(currentStep)
        })
      })
    })
    val evaluatedModel = SingleModelResults(ID,nbSteps,grid,initialConditions, checkCalibrated(grid),modelName,calculateRSME(grid))
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime,evaluatedModel)
    evaluatedModel
  }

   private def createAndInitializeGrid(initialValues:List[InitialCondition]):Grid= {

    val variableList:List[CalculationParametersIOLabels] = defineVariableList
      val theGrid = GridFactory(gridGeometry, nbSteps, variableList)
      val defaultValues = variableList.map(v => v.defaultValue)
      theGrid.initializeGrid(variableList,defaultValues)

      initialValues.foreach(ic => ic.values.foreach(icv =>
        theGrid.setAtCell(ic.variable,icv._1,icv._2)(0)))

    theGrid
  }


  private def defineVariableList:List[CalculationParametersIOLabels] = {
    val labelsForInitialization:List[CalculationParametersIOLabels] = initialConditions.map(x => x.variable)
    val labelsForCalculations:List[CalculationParametersIOLabels] = calculations.map(x => x.outputs)

    (labelsForInitialization ++ labelsForCalculations).distinct
  }

  private def checkCalibrated(grid:Grid):Boolean = {
    if (calibrationSet.isEmpty) {
      true
    } else {
     ApplyCalibrationRules(grid,calibrationSet).results
    }
  }

  private def calculateRSME(grid:Grid):Option[Double] = {
    if (calibrationSet.isEmpty) {
      None
    } else {
      ApplyCalibrationRules(grid,calibrationSet).rsme
    }
  }


  private def printOutputString(time:Double,model:SingleModelResults): Unit = {
    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val calibratedMsg:String = if(model.calibrated){" is calibrated."}else{" is not calibrated."}
    print("Model #"+model.ID+" completed in "+timeTaken+calibratedMsg+EOL)
  }

  def formatHoursMinuteSeconds(nannoseconds:Double): String = {
    val totalTimeInSeconds:Double = nannoseconds/10E8
    val hours:Int = (totalTimeInSeconds/(60*60)).toInt
    val secondsRemainingForMinutes =  (totalTimeInSeconds % (60*60)).toInt
    val minutes:Int =(secondsRemainingForMinutes/60).toInt
    val seconds:Int =(secondsRemainingForMinutes % 60).toInt
    val  timeString:String = if(hours>0 && minutes>0){hours+" hours, " + minutes + " minutes, and " + seconds + " seconds"}
    else if(hours==0 && minutes>0){ minutes + " minutes, and " + seconds + " seconds"}
    else {seconds + " seconds"}
    timeString
  }

}
