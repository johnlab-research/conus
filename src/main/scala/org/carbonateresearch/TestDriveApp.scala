/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.carbonateresearch

import org.carbonateresearch.conus.calibration.{Calibrator, ValueEqualTo, InRange, LargerThan, SmallerThan}
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
  val finalAgeList:List[Double] = List(10.0,1.0,11.5)
  val initialAge:ModelVariable[Double] = ModelVariable("Initial age",110.0,"Ma")
  val finalAge:ModelVariable[Double] = ModelVariable("Final age",0.0,"Ma")

  val myFirstModel = new SteppedModel(numberOfSteps,"Eagleford recrystallization")
    .setGrid(2,2)
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
      AllCells(finalAge,finalAgeList),
      PerCell(D47i,List(
        (List(0.756),Seq(1,0)),
        (List(0.456),Seq(0,0)),
        (List(0.566),Seq(1,1)),
        (List(0.676),Seq(0,1)))
    ))
    .defineCalibration(SampleTemp isEqualTo(159.43044964126625) atCells(Seq(1,1)))

 val runnedModel = myFirstModel.run


  Thread.sleep(10000000)

      }
