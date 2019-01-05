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

  val burialHistory = List((126.0,0.0), (0.0,4500.0))
  val geothermalGradient = List((126.0,35.0))
  val surfaceTemperatures = List((126.0, 20.0),(0.0, 20.0))

  val sample1 = new Sample(name = "Sample 1", age = 63.0, stratigraphicDepth = 0.0, D47observed = 0.712, depositionalTemperature = 20.0)
  val sample2 = new Sample(name = "Sample 2", age = 63.0, stratigraphicDepth = 800.0, D47observed = 0.600, depositionalTemperature = 67.0)

  val sampleList = List(sample1, sample2)


  val model1 = new ThermalHistorySimulation(ageStep = 1,
    burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

  val firstResults: ListBuffer[CalculationStep] = model1.results.head.simSteps

  println(firstResults.foreach(j => j.finalTemp))

        stage = new JFXApp.PrimaryStage {
          title = "DiageneSim"
          scene = new Scene {
            root = new ScatterChart(NumberAxis("X", 0, 6, 1), NumberAxis("Y", 0, 6, 1)) {
              title = "Scatter Chart"
              legendSide = Side.Right
              data = ObservableBuffer(
                xySeries("Series 1", Seq((0.1, 0.2), (1.1, 0.8), (1.9, 2.5), (3.2, 3.3), (3.9, 3.5), (5.1, 5.4))),
                xySeries("Series 2", Seq((0, 4), (1, 1), (2, 4.5), (3, 3.5), (4, 4.25), (5, 4.5))),
                xySeries("Series 3", Seq((0, 1), (1, 2.55), (2, 4), (3, 3), (4, 4.5), (5, 5.5))))
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


