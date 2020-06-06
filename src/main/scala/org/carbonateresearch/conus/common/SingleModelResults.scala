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
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.grids.{Grid, GridElement}
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps

case class SingleModelResults(ID:Int,
                              nbSteps:Int,
                              theGrid:Grid,
                              initialConditions:List[InitialCondition],
                              calibrated:Boolean = false) extends SimulationResults {
val EOL = lineSeparator()
  def size:Int = theGrid.size

  def prettyPrint[T](k:ModelVariable[T],step:Int, coordinate:Seq[Int]):String = {
    theGrid.getVariableForTimeStep(k)(step).toString()
  }

  def completeModelResultsString: String = {
    theGrid.summary
  }

  def isDefinedAt(step:Int):Boolean = {
   // if(Option(theGrid.getTimeStep(step)) == Some[GridElement])
    true
  }

  def isDefinedAt(step:Int, coordinates:Seq[Int]):Boolean = {
    // if(Option(theGrid.getTimeStep(step)) == Some[GridElement])
    true
  }

  def variableIsDefinedAt[T](k:ModelVariable[T],step:Int, coordinates:Seq[Int]):Boolean = {
   // theGrid(step).isDefinedAt(k)
    true
  }

  //def resultsForStep(stepNumber: Int): GridElement = theGrid.getTimeStep(stepNumber)

  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): GridElement= {
    theGrid.getVariableForTimeStep(k)(stepNumber)
  }

  def getStepResult(stepNumber:Int, k:CalculationParametersIOLabels): GridElement = {
    theGrid.getVariableForTimeStep(k)(stepNumber)
  }

  def  getModelVariablesForStep(step:Int):List[CalculationParametersIOLabels] = theGrid.variableList

  private def lastStepNumber:Int = theGrid.nbSteps
  //private def lastStep:GridElement = theGrid.getTimeStep(lastStepNumber)

  def summary: String = ""

}
