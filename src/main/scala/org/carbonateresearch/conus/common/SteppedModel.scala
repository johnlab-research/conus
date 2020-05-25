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

package org.carbonateresearch.conus.common

class SteppedModel (nbSteps:Int, modelName:String="no name",gridGeometry:Seq[Int]=Seq(1))  {
  val prepareSteps:  List[Int] = (0 until nbSteps+1).toList

  def setGrid(gridDimensions:Int*):SteppedModel = {
    new SteppedModel(nbSteps,modelName,gridDimensions)
  }

  def defineMathematicalModel(calculationList: Calculator*): SteppedModelWithCalculations = {
    val mathematicalModel:List[Calculator] = calculationList.toList
    SteppedModelWithCalculations(nbSteps:Int, modelName:String, gridGeometry:Seq[Int],mathematicalModel:List[Calculator])
  }


}

