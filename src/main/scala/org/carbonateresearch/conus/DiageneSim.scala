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
import org.carbonateresearch.conus.common._
import org.carbonateresearch.domainespecific.Geology.PasseyHenkesClumpedDiffusionModel._
import org.carbonateresearch.domainespecific.Geology.GeneralGeology._
import org.carbonateresearch.conus.grids.Grid

import scala.math.{abs, exp, pow}


object DiageneSim extends App {

  val burialHistory = List((110.0,0.0), (75.0,25.0), (50.0,5000.0),(38.0,0.0),(0.0,10.0))
  //val burialHistory = List((110.0,0.0), (50.0,1000.0),(0.0,100.0))
  val geothermalGradientHistory= List((105.0,30.0),(38.0, 30.0),(0.0,30.0))
  val surfaceTemperaturesHistory = List((110.0,30.0),(38.0, 30.0),(0.0,30.0))
  val numberOfSteps = 150
  val ageList:List[Double] = List(50,51,52,53,54,55,56,57.58,59,60,64)
  val initialAge:ModelVariable[Double] = ModelVariable("Initial age",110.0,"Ma")
  val finalAge:ModelVariable[Double] = ModelVariable("Final age",0.0,"Ma")

  val initialModelConditions = InitializeValues(List((initialAge,List(110.0,112.0)),
    (finalAge,List(0.0,10.0)),
    (D47i,List(0.731))))

  val myFirstModel = new SteppedModel(numberOfSteps)
    .defineInitialModelConditions(initialModelConditions)
    .defineMathematicalModelPerCell(
      age =>> ageOfStep(initialAge,finalAge),
  depth =>> burialDepthFromAgeModel(age,burialHistory),
  surfaceTemperature =>> surfaceTemperaturesAtAge(age, surfaceTemperaturesHistory),
  geothermalGradient =>> geothermalGradientAtAge(age,geothermalGradientHistory),
  burialTemperature =>> burialTemperatureFromGeothermalGradient(surfaceTemperature,depth,geothermalGradient),
  dT =>> dTFun,
      D47eq =>> D47eqFun,
  D47i =>> D47iFun,
  SampleTemp =>> davies19_T)


  val timeout = Timeout(5.minutes)
  val runnedModel = myFirstModel.run

  val wtf = Grid(100,100,20)
  println(wtf)
  Thread.sleep(10000000)
  /*

  val initialValues:List[(CalculationParametersIOLabels,List[Any])] = List(
    (D47i,List(0.60,0.607,0.610,0.612,0.613,0.615,0.616)),
    (depositionalAge,ageList),
    (GeothermalGradient,List(30.0,31.0,33.5,33.6,33.7,33.8,33.9,34.0))
  )
 val myDoubleList: List[Double] = List(0.600,0.607,0.612)

 val b:OldModelCalculationSpace = new OldSteppedModel(numberOfSteps)
   .defineInitialModelConditions(
     OldInitializeValues(initialValues))
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
  def handleResults(modelResults: List[OldSingleModelWithResults]) = {
    val tolerance = (35.0, 38.0)

    val validResults = modelResults.filter(p => (tolerance._1<(
      p.finalResult(SampleTemp)) && tolerance._2>(p.finalResult(SampleTemp))))

    println("Found "+validResults.size+" calibrated models:")
    validResults.map(r => r.summary+EOL).foreach(println)
    print("")
  }
*/


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


