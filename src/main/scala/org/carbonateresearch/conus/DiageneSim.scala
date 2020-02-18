package org.carbonateresearch.conus
/*
import scala.collection.mutable.ListBuffer
import scala.collection.Map
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.ScatterChart
import scalafx.scene.chart.XYChart*/
import akka.util.Timeout
import scala.concurrent.duration._
import org.carbonateresearch.conus.common.{ChainableCalculation, ModelCalculationSpace, ModelCalibrationSet, SingleModelWithResults, SteppedModel}
import org.carbonateresearch.conus.calculationparameters.parametersIO._

import scala.compat.Platform.EOL
import org.carbonateresearch.conus.calculationparameters.{CalculateBurialDepthFromAgeModel, CalculateBurialTemperatureFromGeothermalGradient, CalculateStepAges, CalculateStepValue, GeothermalGradientThroughTime, InitializeValues, Initializer, InterpolateValues, SurfaceTemperaturesThroughTime}
import org.carbonateresearch.conus.clumpedThermalModels.PasseyHenkesClumpedDiffusionModel



object DiageneSim extends App with StandardsParameters with PasseyHenkesClumpedDiffusionModel {



  val burialHistory = List((110.0,0.0), (100.0,150.0), (50.0,4766.0),(38.0,0.0),(0.0,0.0))
  val geothermalGradient = List((105.0,30.0),(38.0, 30.0),(0.0,30.0))
  val surfaceTemperatures = List((105.0,30.0),(38.0, 30.0),(0.0,30.0))
  val numberOfSteps = 200
  val depositionalAge = Parameter("Initial age of deposition", " Ma", Some(0), precision = 3)
  val ageList:List[Double] = List(50,51,52,53,54,55,56,57.58,59,60,64)


  val initialValues:List[(CalculationParametersIOLabels,List[Double])] = List(
    (D47i,List(0.60,0.607,0.610,0.612,0.613,0.615,0.616)),
    (depositionalAge,ageList),
    (GeothermalGradient,List(30.0,31.0,33.5,33.6,33.7,33.8,33.9,34.0))
  )
 val myDoubleList: List[Double] = List(0.600,0.607,0.612)

 val b:ModelCalculationSpace = new SteppedModel(numberOfSteps)
   .defineInitialModelConditions(
     InitializeValues(initialValues))
   .defineMathematicalModelPerCell(
    CalculateStepAges(110,0),
    CalculateBurialDepthFromAgeModel(List((110.0,0.0), (100.0,150.0), (50.0,3500.0),(38.0,0.0),(0.0,0.0))),
    InterpolateValues(output = SurfaceTemperature, inputValueLabel = Age, xyList = surfaceTemperatures),
    CalculateBurialTemperatureFromGeothermalGradient(geothermalGradient),
    CalculateStepValue(dT).applyingFunction(dTFun).withParameters(Previous(BurialTemperature), BurialTemperature),
    CalculateStepValue(D47eq).applyingFunction(D47eqFun).withParameters(BurialTemperature),
    CalculateStepValue(D47i).applyingFunction(D47iFun).withParameters(Previous(D47i,TakeCurrentStepValue),D47eq, BurialTemperature,dT),
    CalculateStepValue(SampleTemp).applyingFunction(davies19_T).withParameters(D47i))
   .calibrationParameters(
     ModelCalibrationSet(SampleTemp,35.0,38.0))

val timeout = Timeout(5.minutes)
  val runnedModel = b.run

Thread.sleep(1000000000)
  def handleResults(modelResults: List[SingleModelWithResults]) = {
    val tolerance = (35.0, 38.0)

    val validResults = modelResults.filter(p => (tolerance._1<(
      p.finalResult(SampleTemp)) && tolerance._2>(p.finalResult(SampleTemp))))

    println("Found "+validResults.size+" calibrated models:")
    validResults.map(r => r.summary+EOL).foreach(println)
    print("")
  }



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
/*
  override def stopApp(): Unit =
    actorSystem.terminate()
    super.stopApp()*/
      }


