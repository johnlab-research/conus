package diagenesim

import scala.collection.mutable.ListBuffer
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.ScatterChart
import scalafx.scene.chart.XYChart
import thermalmodel.{Sample, ThermalHistorySimulation, CalculationStep}


object DiageneSim extends JFXApp {

  val burialHistory = List((126.0,0.0), (0.0,6500.0))
  val geothermalGradient = List((126.0,50.0))
  val surfaceTemperatures = List((126.0, 20.0),(0.0, 20.0))

  val sample1 = new Sample(name = "Sample 1", age = 63.0, stratigraphicDepth = 0.0, D47observed = 0.712, depositionalTemperature = 20.0)
  val sample2 = new Sample(name = "Sample 2", age = 63.0, stratigraphicDepth = 800.0, D47observed = 0.600, depositionalTemperature = 67.0)

  val sampleList = List(sample1, sample2)


  val model1 = new ThermalHistorySimulation(ageStep = 1,
    burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

  val firstResults: ListBuffer[CalculationStep] = model1.results.head.simSteps

  val series1D47Env = firstResults.map(_.D47eq)
  val series1D47sample = firstResults.map(_.D47iFinal)

  val series1 = series1D47Env.zip(series1D47sample)

  val plotMaximum = series1D47Env.max
  val plotMinimum = series1D47Env.min

  series1.foreach(println(_))

        stage = new JFXApp.PrimaryStage {
          title = "DiageneSim"
          scene = new Scene {
            root = new ScatterChart(NumberAxis("D47Env", plotMinimum, plotMaximum, .1), NumberAxis("D47Obs", plotMinimum, plotMaximum, .1)) {
              title = "Scatter Chart"
              legendSide = Side.Right
              data = ObservableBuffer(
                xySeries("D47 env vs D47 obs", series1)),
            }
          }
        }

        /** Create XYChart.Series from a sequence of number pairs. */
        def xySeries(name: String, data: Seq[(Double, Double)]) =
          XYChart.Series[Number, Number](
            name,
            ObservableBuffer(data.map {case (x, y) => XYChart.Data[Number, Number](x, y)})
          )

      }


