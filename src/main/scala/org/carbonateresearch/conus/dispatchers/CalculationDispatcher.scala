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
import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}
import scala.Console._
import scala.concurrent.Future

trait CalculationDispatcher {
def calculateModelsList(models:List[SingleModel]):Future[List[SingleModelResults]] = {???}
val typeOfDispatcher:String = "Not specified"
  def outputString(models:List[SingleModel]):String = {
    val EOL = lineSeparator()
    val nbModels = models.size
    val model = models.head
    val gridSize:Int = model.grid.gridGeometry.product
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
}
