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

  val burialHistory = List((126.0,0.0), (90.0, 4500.0), (0.0, 0.0))
  val geothermalGradient = List((126.0,50.0))
  val surfaceTemperatures = List((126.0, 20.0),(0.0, 20.0))

  val sample1 = new Sample(name = "Sample 1", age = 126.0, stratigraphicDepth = 0.0, D47observed = 0.712, depositionalTemperature = 20.0)
  val sample2 = new Sample(name = "Sample 2", age = 63.0, stratigraphicDepth = 800.0, D47observed = 0.600, depositionalTemperature = 67.0)

  val sampleList = List(sample1, sample2)


  val model1 = new ThermalHistorySimulation(ageStep = 1,
    burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

  val firstResults: ListBuffer[CalculationStep] = model1.results.head.simSteps

  val time = firstResults.map(_.myAge)
  val temperature = firstResults.map(_.finalTemp)

  val series1 = time.zip(temperature)


  series1.foreach(println(_))

        stage = new JFXApp.PrimaryStage {
          title = "DiageneSims"
          scene = new Scene {
            root = new LineChart(NumberAxis("Temperature-time", time.min - 5, time.max + 5, 10), NumberAxis("D47Obs", temperature.min-5, temperature.max+5, 10)) {
              title = "D47 evolution"
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

  /*implicit def convertTuppleToDouble (values: List[(AnyVal, Double)]): List[(Double, Double)]  = {
    val x = values(0)._1.toString.toDouble
    val y = values(1)._2.toDouble
    List((x,y))
  }*/

      }


