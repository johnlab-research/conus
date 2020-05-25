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

package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.CalculationParametersIOLabels

trait Grid {
  type TimeStep = Int
  type Dimensions = Seq[Int]
  def gridGeometry:Dimensions
  def size:Int = gridGeometry.product
  def variableMap:Map[CalculationParametersIOLabels,Int]
  def nbSteps: Int
  def numberOfCells:Int = gridGeometry.product * nbSteps

  def allGridCells:List[Seq[Int]] = {
    gridGeometry.size match {
      case 0 => List(Seq(1))
      case 1 => (0 until gridGeometry.head).map(x=>Seq(x)).toList
      case 2 => {
        val firstCoord:Seq[Int] = (0 until gridGeometry.head)
        val secondCoord:Seq[Int] = (0 until gridGeometry(1))
        for {
          f <- firstCoord
          s <- secondCoord
        } yield Seq(f,s)
      }.toList
      case 3 => {
        val firstCoord:Seq[Int] = (0 until gridGeometry.head)
        val secondCoord:Seq[Int] = (0 until gridGeometry(1))
        val thirdCoord:Seq[Int] = (0 until gridGeometry(2))
        for {
          f <- firstCoord
          s <- secondCoord
          t <- thirdCoord
        } yield Seq(f,s,t)
      }.toList
    }
  }

  override def toString: String = {
    "Grid with "+numberOfCells+" cells in "+gridGeometry.size+" dimensions"
  }
  def printGridResults:String
  def summary:String
  def getVariableAtCellForTimeStep(key:CalculationParametersIOLabels, coordinates:Seq[Int])(implicit timeStep: TimeStep):Any
  def getVariableForTimeStep(key:CalculationParametersIOLabels)(implicit timeStep: TimeStep):GridElement
  def setAtCell[T](key:CalculationParametersIOLabels,value:T, coordinates:Dimensions)(implicit timeStep: TimeStep):Unit
  def initializeGrid(keys:Seq[CalculationParametersIOLabels],values:Seq[Any]):Unit
}
