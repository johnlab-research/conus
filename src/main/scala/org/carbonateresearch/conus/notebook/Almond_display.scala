package org.carbonateresearch.conus.notebook

import java.lang.System.lineSeparator

import jupyter.Displayers
import org.carbonateresearch.conus.common._
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


  Displayers.register(classOf[ModelResults], (allResults: ModelResults) => {
    Map(
      "text/html" -> {
        table(cls := "table")(
          tr(th("Model#"), th("Initial model conditions"),th("Calibration"),th("RSME")),
          for(SingleModelResults(id,nbSteps,theGrid,initialConditions,calibrated,modelName,rsme) <-allResults.results) yield(
            tr(td(id),
              td(table(cls := "table")(for((variable,values)<- initialConditions.tail.map(ic => (ic.variable.name,ic.values.map(v =>
              {v._1.toString + {if(v._2.isEmpty){" at all cells"}else {" at cell "+v._2.toString.drop(4)+", "}}}).foldLeft("")(_ + _))))
                yield(tr(td(variable),td(values))))),
              td(if(calibrated){"Calibrated"}else{"Not calibrated"}),
              td(rsme match {
                case Some(v) => v.toString
                case None => "NA"
              }))
            )
        ).render
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
        table(cls := "table")(
          tr(th("Cell coordinates"),for(variable <-allVariables) yield(th(variable.name))),
          for(c<-allCells)yield(
            tr(td(c.toString.drop(4)),
              for(variable <-allVariables) yield(
                td({
                  theGrid.getVariableAtCellForTimeStep(variable,c)(finalStep).toString.take(6)}))))
        ).render
      }
    ).asJava
  })
}
