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

package org.carbonateresearch.conus.dispatchers

import java.lang.System.lineSeparator
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import org.carbonateresearch.conus.common.{ModelCalculationSpace, SingleModel, SingleModelResults}
import org.carbonateresearch.conus.simulators.AkkaCentralSimulatorActor
import scala.Console.{BLUE, MAGENTA, RESET, UNDERLINED, WHITE, YELLOW}


object CalculationDispatcherAkka {

  var EOL = lineSeparator()

  def apply(): Behavior[runBehaviour] = Behaviors.setup { context =>
    var initialCount: Int = 0
    var thisModelCalculationSpace: ModelCalculationSpace = null
    val t0 = System.nanoTime()
    var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelResults]
    var initialMessage: String = "None"
    var EOL = lineSeparator()

    Behaviors.receive { (context, message) =>
      message match {
        case RunMultipleModels(modelsToRun, pid, autoSave, loggerType) => {
          loggerType match {
            case l: AlmondLoggerType => EOL = "<br>"
          }

          val batchLogger: ActorRef[runBehaviour] = context.spawn(EventLogger(), s"Process-$pid-logger")

          val runStartedMessage: runBehaviour = setupMessage(loggerType, batchLogger,
            "----------------------------------------" + lineSeparator() + "EVALUATING MODEL" + lineSeparator() + "----------------------------------------")

          batchLogger ! runStartedMessage

          val dateAndTime: String = new SimpleDateFormat("/yyyy-MM-dd-hh-mm-ss").format(new Date)

          thisModelCalculationSpace = modelsToRun
          initialCount = modelsToRun.models.size
          initialMessage = dateAndTime + lineSeparator() + runStartOutputString(modelsToRun.models)

          val runDetailsMessage: runBehaviour = setupMessage(loggerType, batchLogger, initialMessage)
          batchLogger ! runDetailsMessage

          modelsToRun.models.foreach(m => {
            val id = m.ID
            val runner = context.spawn(SingleModelRunnerAkka(), s"Process-$pid-runner-$id")
            runner ! RunSingleModel(m, t0, dateAndTime, context.self, autoSave, batchLogger, loggerType)
          })
          Behaviors.same
        }
        case ResultsSingleRun(singleRunResult, batchLogger, loggerType) => {
          resultsList += singleRunResult
          val percentComplete: Double = (resultsList.size.toDouble / initialCount.toDouble) * 100
          val hashLine = "#" * percentComplete.ceil.toInt + " "
          val progress = "Run progress:" + EOL + hashLine + f"$percentComplete%1.1f" + "%"
          val currentProgressMessage: runBehaviour = setupMessage(loggerType, batchLogger, progress)
          batchLogger ! currentProgressMessage
          AkkaCentralSimulatorActor.updateModelList(thisModelCalculationSpace, resultsList.toList)
          if (resultsList.size == initialCount) {

            val timeDifference:String = "Total runtime: "+getHoursMinuteSeconds(System.nanoTime()-t0)
            val finalProgressMessage: runBehaviour = setupMessage(loggerType, batchLogger, progress+EOL+timeDifference)
            batchLogger ! finalProgressMessage
            Behaviors.stopped
          } else {
            Behaviors.same
          }
        }
        case _ => {
          Behaviors.stopped
        }
      }
    }
  }

  def setupMessage(loggerType:LoggerType, myLogger:ActorRef[runBehaviour],theMessage:String):runBehaviour = {
    loggerType match {
      case l:ConsoleLoggerType => ConsolePrintableMessage(theMessage,myLogger)
      case l:AlmondLoggerType => AlmondPrintableMessage(theMessage,l.kernel,l.cellID,myLogger)}}

  def runStartOutputString(models:List[SingleModel]):String = {

    val nbModels = models.size
    val model = models.head
    val gridSize:Int = model.gridGeometry.product
    val nbOperation = model.calculations.size*gridSize
    val nbSteps = model.nbSteps
    val totalNumberOperations = (nbModels*nbSteps*nbOperation)
    val totNbOperationString:String = totalNumberOperations.toString
    val strLen = totNbOperationString.length
    val remainingChar = strLen%3
    val groupsOfThrees:Int = ((strLen-remainingChar)/3) - {if(remainingChar == 0){1}else{0}}

    val positions = (1 to groupsOfThrees).reverse.toList

    def placeTick(currentString: String, pos: List[Int]): String = {
      pos match {
        case Nil => {currentString}
        case x::xs => {placeTick(currentString, xs).patch(strLen-(x*3), "'", 0)}
      }
    }

    val totNbOperationStringFormatted = placeTick(totNbOperationString,positions)
    val nbCPUs: String = java.lang.Runtime.getRuntime.availableProcessors.toString
    s"RUN DATA" + EOL +
      s"Total number of models:" + nbModels.toString + EOL +
      s"Number of cell per grid: " + gridSize + EOL +
      s"Number of steps per model: " + nbSteps + EOL +
      s"Number of operation per step: " + nbOperation + EOL +
      s"Total number of operations: "+ totNbOperationStringFormatted + EOL +
      s"Available CPU cores: "+ nbCPUs + EOL +
      s"----------------------------------------" + EOL + s"RUN PROGRESS"

  }

  def getHoursMinuteSeconds(nannoseconds:Double): String = {
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
