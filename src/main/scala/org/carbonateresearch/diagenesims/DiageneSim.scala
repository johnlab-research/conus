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
import org.carbonateresearch.diagenesims.common.{AbstractSimulationParameters, ChainableCalculation, NumberWrapper, ParrallelModellerDispatcherActor, Stepper}
import org.carbonateresearch.diagenesims.calculationparameters.parametersIO._
import org.carbonateresearch.diagenesims.clumpedThermalModels.{ClumpedEquations, ClumpedSample, ThermalCalculationStepOld, ThermalClumpedSimulationParameter, ThermalHistorySimulationOld}
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import scala.compat.Platform.EOL

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import org.carbonateresearch.diagenesims.calculationparameters.{AgesFromMaxMinCP, BurialTemperatureCP, CalculationResults, GeothermalGradientHistoryCP, InterpolatorCP, SurfaceTemperaturesHistoryCP, FunctionCP}
import spire.implicits._
import spire.math._
import spire.algebra._


object DiageneSim extends JFXApp with NumberWrapper {


  val actorSystem = ActorSystem("Diagenesim-Akka")
  private val modeller = actorSystem.actorOf(Props[ParrallelModellerDispatcherActor])

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + ((t1 - t0)/10E9) + " seconds")
    result
  }

  val burialHistory = List((30.0,0.0), (25.0,3000.0), (20.0,6500.0))
  val geothermalGradient = List((110.0,30.0),(0.0,20.0))
  val surfaceTemperatures = List((110.0,30.0),(0.0, 25.0))
  val numberOfSteps = 130

  //val doubleFun = (x: List[Number]) => x.map(_*2)
  //val doubler = FunctionCP(List(Depth), List(Doubler()), doubleFun)

  val doubleFun = (x: Number) => x * 2
  val quadrupleFun = (x: Number, y:Number) => x * y
  val simpleQuadrupler = quadrupleFun(Number(2), _: Number)

  val doubler = FunctionCP(Depth, doubleFun, Doubler(0))

  case object Quadrupler extends CalculationParametersIOLabels {override def toString: String = "My Quadrupler"}

  val quadrupler = FunctionCP((Doubler(), Doubler()), quadrupleFun, Quadrupler)

  val a: List[ChainableCalculation] = (500 to 1000 by 5).map(x =>
   Stepper(numberOfSteps) |-> AgesFromMaxMinCP(110,0) |->
    InterpolatorCP(outputValueLabel = Depth, inputValueLabel = Age, xyList =List((70.0,0.0), (65.0,450.0), (50.0,x))) |->
    InterpolatorCP(outputValueLabel = GeothermalGradient, inputValueLabel = Age, xyList =geothermalGradient) |->
    InterpolatorCP(outputValueLabel = SurfaceTemperature, inputValueLabel = Age, xyList =surfaceTemperatures) |->
    BurialTemperatureCP(geothermalGradient) |->
  doubler |->
  quadrupler).toList


case class MyGod(n:Int =0) extends CalculationParametersIOLabels


  def handleResults(modelResults: List[CalculationResults]) = {
    val tolerance = Interval(Number(45), Number(48))

    val validResults = modelResults.filter(p => tolerance.contains(
      p.finalResult(BurialTemperature)) )
    println("Found "+validResults.size+" calibrated models:")
    validResults.map(r => "Model ->"+r.summary+EOL).foreach(println)
    print("")
  }

  modeller ! a

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

*/

  override def stopApp(): Unit =
    actorSystem.terminate()
    super.stopApp()
      }


