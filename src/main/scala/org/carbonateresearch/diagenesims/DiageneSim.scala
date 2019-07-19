package org.carbonateresearch.diagenesims

import scala.collection.mutable.ListBuffer
import scala.collection.Map
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.ScatterChart
import scalafx.scene.chart.XYChart
import org.carbonateresearch.diagenesims.ageSteppedModels.{AbstractSimulationParameters, ChainableCalculation, Stepper}
import org.carbonateresearch.diagenesims.clumpedThermalModels.{ClumpedEquations, ClumpedSample, ForwardThermalClumpedModeller, ThermalCalculationStepOld, ThermalClumpedSimulationParameter, ThermalHistorySimulationOld}
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.carbonateresearch.diagenesims.calculationparameters.{AgesFromMaxMinCP, InterpolatorCP, MultiplierFromStepsCP}
import org.carbonateresearch.measurementvalues.NumberWithErrors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import spire.implicits._
import spire.math._
import spire.algebra._



object DiageneSim extends JFXApp {


  /*val actorSystem = ActorSystem("Diagenesim-Akka")
  val Modeller = actorSystem.actorOf(Props[ForwardThermalClumpedModeller])*/

  val burialHistory = List((Number(30.0),Number(0.0)), (Number(25.0),Number(3000.0)), (Number(20.0),Number(6500.0)))
  val geothermalGradient = List((Number(110.0),Number(30.0)),(Number(0.0),Number(30.0)))
  val surfaceTemperatures = List((Number(110.0), Number(30.0)),(Number(0.0), Number(25.0)))
  val numberOfSteps = 10

  val a = Stepper(numberOfSteps) + AgesFromMaxMinCP(Number(30.0),Number(20.0)) + InterpolatorCP(outputValueLabel = "Depth", inputValueLabel = "Age", xyList =burialHistory)

  print(a)
  println("      ")
  print(a.evaluate)


/*
  private final case class tryMe[A](value: spire.math.Fractional[A], error:spire.math.Fractional[A]) {
    override def toString(): String = (value.toString+" ± "+error.toString)
  }

  println(tryMe[Double](20.3,23.01))



  val sample1 = new ClumpedSample(name = "cc1", depositionalAge = 82.0, D47observed = 0.712, depositionalTemperature = 25.0)
  val sample2 = new ClumpedSample(name = "cc2", depositionalAge = 82.0, depositionalDepth=450, D47observed = 0.450, depositionalTemperature = 0.0)
  val sample3 = new ClumpedSample(name = "cc3", depositionalAge = 55.0, D47observed = 0.712, depositionalTemperature = 40.0)
  val sample4 = new ClumpedSample(name = "cc4", depositionalAge = 20.0, D47observed = 0.600, depositionalTemperature = 40.0)

  val sampleList = List(sample1, sample2, sample3, sample4)

  val modelParameter = Map("Burial history" -> burialHistory, "geothermal gradient" -> geothermalGradient, "surface temperatures" -> surfaceTemperatures,
                            "number of steps" -> numberOfSteps, "sample list" -> sampleList)

  Modeller ! sampleList


  val model1 = new ThermalHistorySimulationOld(ageStep = 1,
    burialHistory = burialHistory, samples = sampleList, geothermalGradient = geothermalGradient, surfaceTemp = surfaceTemperatures)

  val series = model1.results.map(a => a.simSteps.map(_.myAge).zip(a.simSteps.map(_.finalTemp)).filter(p => p._1 <= a.age))


  val firstResults: ListBuffer[ThermalCalculationStepOld] = model1.results.head.simSteps

  val time = firstResults.map(_.myAge)
  val temperature = firstResults.map(_.finalTemp)

  val series1 = time.zip(temperature)


  val dataset = ObservableBuffer(
    xySeries("Sample 1", series(0)),
    xySeries("Sample 1 target", Seq((0.0, ClumpedEquations.davies19_T(sample1.D47observed)))),
    xySeries("Sample 2", series(1)),
    xySeries("Sample 2 target", Seq((0.0, ClumpedEquations.davies19_T(sample2.D47observed)))),
    xySeries("Sample 3", series(2)),
    xySeries("Sample 3 target", Seq((0.0, ClumpedEquations.davies19_T(sample3.D47observed)))),
    xySeries("Sample 4", series(3)),
    xySeries("Sample 4 target", Seq((0.0, ClumpedEquations.davies19_T(sample4.D47observed)))))

  series.foreach(_.foreach(println))



val maxTime = sampleList.map(a => a.depositionalAge).max+5
val minTemp = series.flatMap(a => a.map(b => b._2)).min-5
  val maxTemp = series.flatMap(a => a.map(b => b._2)).max+5



        stage = new JFXApp.PrimaryStage {
          title = "DiageneSims"
          scene = new Scene {
            root = new LineChart(NumberAxis("Time (Ma)",0, maxTime, 10), NumberAxis("Temperature Observed", minTemp, maxTemp, 10)) {
              title = "D47 evolution"
              legendSide = Side.Right
              data = dataset
            }
          }
        }

        /** Create XYChart.Series from a sequence of number pairs. */
        def xySeries(name: String, data: Seq[(Double, Double)]) =
          XYChart.Series[Number, Number](
            name,
            ObservableBuffer(data.map {case (x, y) => XYChart.Data[Number, Number](x, y)})
          )

actorSystem.terminate()*/

      }


