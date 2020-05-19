package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.Grid
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps
import org.carbonateresearch.conus.util.TimeUtils
import java.lang.System.lineSeparator
import scala.annotation.tailrec

case class SingleModel(ID:Int,nbSteps:Int,grid:Grid,
                       calculations:List[Calculator],
                       initialConditions:List[InitialCondition]) extends Combinatorial {
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
    val evaluatedModel = SingleModelResults(ID,nbSteps,grid,initialConditions)
    val currentTime = System.nanoTime()
    printOutputString(currentTime-startTime,evaluatedModel)
    evaluatedModel
  }



  private def printOutputString(time:Double,model:SingleModelResults): Unit = {
    val timeTaken:String = TimeUtils.formatHoursMinuteSeconds(time)
    val nbChar = timeTaken.length + ID.toString.length + 25
    val deleteSequence:String = "\b"*nbChar
    print("Model #"+model.ID+" completed in "+timeTaken+":"+EOL + model.summary)
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
