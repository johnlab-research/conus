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
import org.carbonateresearch.conus.util.TimeUtils
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.calibration.{ApplyCalibrationRules, Calibrator}

case class SingleModel(ID:Int,nbSteps:Int,grid:Grid,
                       calculations:List[Calculator],
                       initialConditions:List[InitialCondition],
                       calibrationSet:List[Calibrator]=List()) extends Combinatorial {
  val EOL:String = lineSeparator()
  val steps:Seq[Int] = (0 until nbSteps).toList
  val geometry:Seq[Int] = grid.gridGeometry
  private val allGridCells:List[Seq[Int]] = {
    geometry.size match {
      case 0 => List(Seq(1))
      case 1 => (0 until geometry.head).map(x=>Seq(x)).toList
      case 2 => {
        val firstCoord:Seq[Int] = (0 until geometry.head)
        val secondCoord:Seq[Int] = (0 until geometry(1))
        for {
          f <- firstCoord
          s <- secondCoord
        } yield Seq(f,s)
      }.toList
      case 3 => {
        val firstCoord:Seq[Int] = (0 until geometry.head)
        val secondCoord:Seq[Int] = (0 until geometry(1))
        val thirdCoord:Seq[Int] = (0 until geometry(2))
        for {
          f <- firstCoord
          s <- secondCoord
          t <- thirdCoord
        } yield Seq(f,s,t)
        }.toList
    }
  }

  def evaluate(startTime:Double): SingleModelResults = {
    steps.foreach(s => {
      calculations.foreach(c => {
        allGridCells.foreach(coordinates => {
          val currentStep = Step(s,coordinates,grid,"")
          c.calculate(currentStep)
        })
      })
    })
    val evaluatedModel = SingleModelResults(ID,nbSteps,grid,initialConditions, checkCalibrated)
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime,evaluatedModel)
    evaluatedModel
  }

  private def checkCalibrated:Boolean = {
    if (calibrationSet.isEmpty) {
      true
    } else {
     ApplyCalibrationRules(grid,calibrationSet).results
    }
  }

  private def printOutputString(time:Double,model:SingleModelResults): Unit = {
    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar
    val calibratedMsg:String = if(checkCalibrated){" is calibrated."}else{" is not calibrated."}
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
