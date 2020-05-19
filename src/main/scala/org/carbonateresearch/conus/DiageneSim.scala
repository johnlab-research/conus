package org.carbonateresearch.conus
import breeze.linalg._
import breeze.numerics._
import org.ojalgo.array.Array2D
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
import scalafx.scene.chart.XYChart
import akka.util.Timeout*/

import scala.concurrent.duration._
import org.carbonateresearch.conus.common._
import org.carbonateresearch.domainespecific.Geology.PasseyHenkesClumpedDiffusionModel._
import org.carbonateresearch.domainespecific.Geology.GeneralGeology._
import org.carbonateresearch.conus.grids.GridFactory

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

  val myFirstModel = new SteppedModel(numberOfSteps,"Eagleford recrystallization")
    .setGrid(5)
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
    .defineInitialModelConditions(InitialConditions(initialAge,List(List(110.0))),
      InitialConditions(finalAge,List(List(10.0))),
      InitialConditions(D47i,List(List(0.731, 0.455), List(0.756), List(0.456),List(0.566),List(0.676)),List(Seq(0),Seq(1),Seq(2),Seq(3),Seq(4)))
    )

 val runnedModel = myFirstModel.run


  Thread.sleep(10000000)

      }



