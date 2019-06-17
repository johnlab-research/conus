package org.carbonateresearch.diagenesims

import scala.collection.mutable.ListBuffer
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.ScatterChart
import scalafx.scene.chart.XYChart
import org.carbonateresearch.diagenesims.thermalmodel.{CalculationStep, Constants, Sample, ThermalHistorySimulation}
import org.carbonateresearch.diagenesims.thermalmodel.{CalculationStep, Sample, ThermalHistorySimulation}


object DiageneSim extends JFXApp {

  val burialHistory = List((85.0,1000.0), (30.0,3000.0), (0.0,5500.0))
  val geothermalGradient = List((85.0,30.0),(0.0,30.0))
  val surfaceTemperatures = List((85.0, 30.0),(0.0, 25.0))

  val sample1 = new Sample(name = "cc1", age = 82.0, stratigraphicDepth = 1000.0, D47observed = 0.712, depositionalTemperature = 50.0)
  val sample2 = new Sample(name = "cc2", age = 65.0, stratigraphicDepth = 0.0, D47observed = 0.600, depositionalTemperature = 37.0)
  val sample3 = new Sample(name = "cc3", age = 55.0, stratigraphicDepth = 0.0, D47observed = 0.712, depositionalTemperature = 40.0)
  val sample4 = new Sample(name = "cc4", age = 55.0, stratigraphicDepth = 0.0, D47observed = 0.600, depositionalTemperature = 40.0)

  val sampleList = List(sample1, sample2, sample3, sample4)


  val model1 = new ThermalHistorySimulation(ageStep = 1,
    burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

  //var allResults: ListBuffer[CalculationStep] = List.empty(CalculationStep)

  val firstResults: ListBuffer[CalculationStep] = model1.results.head.simSteps

  val time = firstResults.map(_.myAge)
  val temperature = firstResults.map(_.finalTemp)

  val series1 = time.zip(temperature)


  series1.foreach(println(_))

        stage = new JFXApp.PrimaryStage {
          title = "DiageneSims"
          scene = new Scene {
            root = new LineChart(NumberAxis("Temperature Environment", time.min - 5, time.max + 5, 10), NumberAxis("Temperature Observed", temperature.min-5, temperature.max+5, 10)) {
              title = "D47 evolution"
              legendSide = Side.Right
              data = ObservableBuffer(
                xySeries("D47 env vs D47 obs", series1))
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


