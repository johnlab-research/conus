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

package org.carbonateresearch.conus.notebook

import java.lang.System.lineSeparator
import jupyter.Displayers
import org.carbonateresearch.conus.common._
import org.carbonateresearch.conus.grids.{GridElement, GridView}
import scalatags.Text.all._

import scala.jdk.CollectionConverters._


class Almond_display {
  private val EOL = lineSeparator()

  def registerDisplays = {

      Displayers.register(classOf[CalculationParametersIOLabels], (variable: CalculationParametersIOLabels) => {
        val modelName = variable.name
        val modelUnit = variable.unitName
        val modelZero = variable.defaultValue

        Map(
          "text/html" -> {
          s"Variable <b>$modelName</b> with initial value of $modelZero $modelUnit  defined"
          }
        ).asJava
      })
  }


  Displayers.register(classOf[SteppedModelWithCalculations], (variable: SteppedModelWithCalculations) => {
        Map(
          "text/html" -> {
            table(cls := "table")(
              tr(th("Feature"), th("Value")),
              tr(
                td("Name"),
                td(variable.modelName)),
              tr(
                td("Nb of steps"),
                td(variable.nbSteps)),
              tr(
                td("Nb grid cells"),
                td(variable.gridGeometry.product)),
              tr(
                td("Nb of operations per step"),
                td(variable.mathematicalModel.length))
            ).render
          }
        ).asJava
      })

      Displayers.register(classOf[ModelCalculationSpace], (variable: ModelCalculationSpace) => {
        Map(
          "text/html" -> {
            "<b>Model characteristics</b>" +
            table(cls := "table")(
              tr(th("Feature"), th("Value")),
              tr(
                td("Name"),
                td(variable.modelName)),
              tr(
                td("Nb of steps"),
                td(variable.models(0).nbSteps)),
              tr(
                td("Nb of models"),
                td(variable.models.length)),
              tr(
                td("Nb grid cells"),
                td(variable.models(0).gridGeometry.product)),
              tr(
                td("Nb of operations per step"),
                td(variable.models(0).calculations.length)),
              tr(
                td("Total nb of operations"),
                td(variable.models(0).calculations.length*variable.models(0).gridGeometry.product*variable.models.length*variable.models(0).nbSteps))
            ).render
          }
        ).asJava
      })

private val tableStyle:String = "align: center; display:block; background-color:fffff0; border: 2px solid green; height: 450px; overflow-y: scroll"
  private val tableHeaderStyle:String = "border: 1px solid green; background-color:#ebebec"
    private val tableCellStyle:String = "border: 1px solid green; background-color:fffff0"
      private val tableRowStyle:String = "border: 1px solid green; background-color:fffff0"
  Displayers.register(classOf[ModelResults], (allResults: ModelResults) => {
    Map(
      "text/html" -> {
        val modelName = allResults.getModel(0).modelName
        val nbModels = allResults.results.length
        val nbTimesteps = allResults.getModel(0).nbSteps
        s"<b>$modelName</b> <br> Contains $nbModels models each comprising $nbTimesteps timesteps" +
          table(cls := "table",style:=tableStyle)(
            tr(th("Model#",style:=tableHeaderStyle),
              th("Initial model conditions",style:=tableHeaderStyle),
              th("RSME",style:=tableHeaderStyle)),
            for(SingleModelResults(id,nbSteps,theGrid,initialConditions,calibrated,modelName,rsme) <-allResults.results) yield(
              tr(style:=tableRowStyle)(td(id,style:=tableCellStyle),
                td(table(cls := "table")(for((variable,values)<- initialConditions.tail.map(ic => (ic.variable.name,ic.values.map(v =>
                {v._1.toString + {if(v._2.isEmpty){" at all cells"}else {" at cell "+v._2.toString.drop(4)+", "}}}).foldLeft("")(_ + _))))
                  yield(tr(td(variable),td(values))))),
                td(rsme match {
                  case Some(v) => v.toString
                  case None => "NA"
                },style:=tableCellStyle))
              )).render
      }
    ).asJava
  })

  Displayers.register(classOf[SingleModelResults], (results: SingleModelResults) => {
    val allCells = results.theGrid.allGridCells
    val finalStep = results.nbSteps-1
    val theGrid = results.theGrid
    val allVariables = theGrid.variableList.tail

    Map(
      "text/html" -> {
        val totalTimestep = theGrid.nbSteps-1
        val modelName = results.modelName
        val ID = results.ID
        s"<b>$modelName model #$ID <br>Timestep: $finalStep</b> [from 0 to $totalTimestep]" +
          table(cls := "table",style:=tableStyle)(
            tr(th("Cell coordinates",style:=tableRowStyle),for(variable <-allVariables) yield(th(variable.name,style:=tableHeaderStyle))),
            for(c<-allCells)yield(
              tr(td(c.toString.drop(4),style:=tableCellStyle),
                for(variable <-allVariables) yield(
                  td({theGrid.getVariableAtCellForTimeStep(variable,c)(finalStep).toString},style:=tableCellStyle)),style:=tableRowStyle))
          ).render
      }
    ).asJava
  })

  Displayers.register(classOf[GridView], (view: GridView) => {
    val allCells = view.theGrid.allGridCells
    val theGrid = view.theGrid
    val allVariables = theGrid.variableList.tail

    Map(
      "text/html" -> {
        val timestepString = view.timestep
        val totalTimestep = view.theGrid.nbSteps-1
        s"<b>Model timestep: $timestepString</b> [from 0 to $totalTimestep]" +
        table(cls := "table", style:=tableStyle)(
          tr(th("Cell coordinates",style:=tableHeaderStyle),for(variable <-allVariables) yield(th(variable.name,style:=tableHeaderStyle)),style:=tableRowStyle),
          for(c<-allCells)yield(
            tr(td(c.toString.drop(4),style:=tableRowStyle),
              for(variable <-allVariables) yield(
                td({{theGrid.getVariableAtCellForTimeStep(variable,c)(view.timestep)}.toString},style:=tableRowStyle)),style:=tableRowStyle))
        ).render
      }
    ).asJava
  })

}
