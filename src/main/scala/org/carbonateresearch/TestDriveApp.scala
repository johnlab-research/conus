package org.carbonateresearch

import org.carbonateresearch.conus.common.{ModelVariable, SteppedModel}
import org.carbonateresearch.conus.grids.{AllCells, PerCell}
import org.carbonateresearch.conus.modelzoo.GeneralGeology._
import org.carbonateresearch.conus.modelzoo.PasseyHenkesClumpedDiffusionModel._

object TestDriveApp extends App {
  val burialHistory = List((110.0,0.0), (75.0,25.0), (50.0,5000.0),(38.0,0.0),(0.0,10.0))
  val geothermalGradientHistory= List((105.0,30.0),(38.0, 30.0),(0.0,30.0))
  val surfaceTemperaturesHistory = List((110.0,30.0),(38.0, 30.0),(0.0,30.0))
  val numberOfSteps = 150
  val ageList:List[Double] = List(110.0,111.0,99.0)
  val initialAge:ModelVariable[Double] = ModelVariable("Initial age",110.0,"Ma")
  val finalAge:ModelVariable[Double] = ModelVariable("Final age",0.0,"Ma")

  val myFirstModel = new SteppedModel(numberOfSteps,"Eagleford recrystallization")
    .setGrid(5)
    .defineMathematicalModel(
      age =>> ageOfStep(initialAge,finalAge),
  depth =>> burialDepthFromAgeModel(age,burialHistory),
  surfaceTemperature =>> surfaceTemperaturesAtAge(age, surfaceTemperaturesHistory),
  geothermalGradient =>> geothermalGradientAtAge(age,geothermalGradientHistory),
  burialTemperature =>> burialTemperatureFromGeothermalGradient(surfaceTemperature,depth,geothermalGradient),
  dT =>> dTFun,
      D47eq =>> D47eqFun,
  D47i =>> D47iFun,
  SampleTemp =>> davies19_T
    )
    .defineInitialModelConditions(
      AllCells(initialAge,ageList),
      AllCells(finalAge,10.0),
      PerCell(D47i,List(
        (List(0.731),Seq(0)),
        (List(0.756),Seq(1)),
        (List(0.456),Seq(2)),
        (List(0.566),Seq(3)),
        (List(0.676),Seq(4)))
    ))

 val runnedModel = myFirstModel.run


  Thread.sleep(10000000)

      }