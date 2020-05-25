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

import org.carbonateresearch.conus.grids.{Grid, GridFactory}

case class Step(stepNumber:Int, coordinates:Seq[Int], grid:Grid, stepErrors:String, stepOffset:Int=0){
  def - (offset:Int):Step = Step(this.stepNumber,this.coordinates,this.grid,this.stepErrors,offset)
  def i: Step = Step(this.stepNumber,this.coordinates,this.grid,this.stepErrors,this.stepNumber)
}

object Step {
  def apply[T](k:ModelVariable[T],v:T):Step = {
    val mv = ModelVariable[Int]("Dummy",0)
    val varMap = Map(mv.asInstanceOf[CalculationParametersIOLabels]->0)
    val grid = GridFactory(Seq(1),1,varMap)
    Step(0,Seq(0),grid,"")
  }
}
