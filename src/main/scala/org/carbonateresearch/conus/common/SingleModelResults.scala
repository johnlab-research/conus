package org.carbonateresearch.conus.common
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.grids.{Grid, GridElement}
import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps

case class SingleModelResults(ID:Int,
                              nbSteps:Int,
                              theGrid:Grid,
                              initialConditions:List[InitialCondition]) extends SimulationResults {
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

  def resultsForStep(stepNumber: Int): GridElement = theGrid.getTimeStep(stepNumber)

  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): GridElement= {
    theGrid.getVariableForTimeStep(k)(stepNumber)
  }

  def getStepResult(stepNumber:Int, k:CalculationParametersIOLabels): GridElement = {
    theGrid.getVariableForTimeStep(k)(stepNumber)
  }

  def  getModelVariablesForStep(step:Int):List[CalculationParametersIOLabels] = theGrid.variableMap.keys.toList

  private def lastStepNumber:Int = theGrid.nbSteps
  private def lastStep:GridElement = theGrid.getTimeStep(lastStepNumber)

  def summary: String = ""

}
