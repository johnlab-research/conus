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

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.common.{ModelCalculationSpace, SingleModel, SingleModelResults}
import org.carbonateresearch.conus.RunScheduler

import scala.Console.{BLUE, MAGENTA, RESET, UNDERLINED, WHITE, YELLOW}

object CalculationDispatcherAkka {
  private val typeOfDispatcher: String = java.lang.Runtime.getRuntime.availableProcessors.toString
  private var initialCount:Int = 0
  private var thisModelCalculationSpace:ModelCalculationSpace = null
  private val t0 = System.nanoTime()
  private var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelResults]

  sealed trait runBehaviour
  final case class RunMultipleModels(modelsToRun: ModelCalculationSpace) extends runBehaviour
  final case class RunSingleModel(theModel: SingleModel, startTime:Double = t0, replyTo: ActorRef[ModelResults]) extends runBehaviour
  final case class ModelResults(theResults: SingleModelResults) extends runBehaviour
  final case class GetIntermediateResults(fromModel: ModelCalculationSpace) extends runBehaviour

  def apply(): Behavior[runBehaviour] = Behaviors.setup { context =>

    Behaviors.receive { (context, message) =>
      message match {
        case RunMultipleModels(modelsToRun) => {
          println("----------------------------------------"+lineSeparator()+"RUN STARTED"+lineSeparator()+"----------------------------------------")
          thisModelCalculationSpace = modelsToRun
          initialCount = modelsToRun.models.size
          println(runStartOutputString(modelsToRun.models))
          modelsToRun.models.foreach(m => {
            val runner = context.spawn(SingleModelRunnerAkka(), "runner"+m.ID)
            runner ! RunSingleModel(m, t0, context.self)
          })
          Behaviors.same
        }
        case ModelResults(theResults) => {
          resultsList.addOne(theResults)
          if (resultsList.size == initialCount) {
            RunScheduler.updateModelList(thisModelCalculationSpace,resultsList.toList)
            println(lineSeparator()+"----------------------------------------"+ lineSeparator() + "END OF RUN"+lineSeparator()+"----------------------------------------")
            val modelFolder:String = System.getProperty("user.home")+"/Conus/"+thisModelCalculationSpace.modelName
            ExcelEncoder.writeExcel(resultsList.toList,modelFolder)
            Behaviors.stopped
          } else {
            Behaviors.same
          }
        }

        case GetIntermediateResults(theModel) => {
      }
    }
  }

  def runStartOutputString(models:List[SingleModel]):String = {
    val EOL = lineSeparator()
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

    s"${RESET}${MAGENTA}${UNDERLINED}RUN DATA${RESET}" + EOL +
      s"${RESET}${YELLOW}Total number of models:${RESET} ${WHITE}" + nbModels.toString + EOL +
      s"${RESET}${YELLOW}Number of cell per grid:${RESET}${WHITE} " + gridSize + EOL +
      s"${RESET}${YELLOW}Number of steps per model:${RESET}${WHITE} " + nbSteps + EOL +
      s"${RESET}${YELLOW}Number of operation per step:${RESET}${WHITE} " + nbOperation + EOL +
      s"${RESET}${YELLOW}Total number of operations:${RESET}${WHITE} "+ totNbOperationStringFormatted + EOL +
      s"${RESET}${YELLOW}Available CPU cores:${RESET}${WHITE} "+ typeOfDispatcher + EOL +
      s"${RESET}${BLUE}----------------------------------------${RESET}" + EOL + s"${RESET}${MAGENTA}${UNDERLINED}RUN PROGRESS${RESET}"

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
