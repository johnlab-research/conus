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

package org.carbonateresearch.conus.grids.universal
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.{Grid, GridElement, GridStringFormatter}
import java.lang.System.lineSeparator
import scala.reflect.{ClassTag}
import scala.collection.immutable.ListMap

case class UniversalGrid(override val gridGeometry:Seq[Int],
                         override val nbSteps:Int,
                         override val variableList:List[CalculationParametersIOLabels],
                         private val underlyingGrid:Map[CalculationParametersIOLabels,TimeStepVector]) extends Grid {

  private val EOL = lineSeparator()

  def getVariableAtCellForTimeStep(key:CalculationParametersIOLabels, coordinates:Seq[Int])(implicit timeStep: TimeStep):Any = {
    underlyingGrid(key).timestep(timeStep).getValueAtCell(coordinates)
  }

  def getVariableForTimeStep(key:CalculationParametersIOLabels)(implicit timeStep: TimeStep):GridElement = {
    underlyingGrid(key).timestep(timeStep)
  }

  def setAtCell[T](key:CalculationParametersIOLabels,value:T, coordinates:Dimensions)(implicit timeStep: TimeStep):Unit = {
    coordinates.size match {
      case 0 => initializeGrid(Seq(key),Seq(value))
      case _ => underlyingGrid(key).timestep(timeStep).setAtCell(value,coordinates)
    }
  }

  def initializeGrid(keys:Seq[CalculationParametersIOLabels],values:Seq[Any]):Unit = {
    keys.foreach(k => (0 until nbSteps).foreach(ts => {
      underlyingGrid(k).timestep(ts).setForAllCells(values(keys.indexOf(k)))
    }))
  }

  override def toString:String = {
    val cString = gridGeometry.size match {
      case 1 => ",X"
      case 2 => ",X,Y"
      case _ => ",X,Y,Z"
    }

    val keys:List[CalculationParametersIOLabels] = variableList.sortBy(cp => cp.name)
    val returnString = "Timestep"+cString+keys.map(x => ","+x.toString).foldLeft("")(_+_)+lineSeparator()+
    (0 until nbSteps).map(s => {
      allGridCells.map(c => (List(s.toString+",")++c.map(cc => cc.toString+",")++keys.map(k => underlyingGrid(k).timestep(s).getValueAtCell(c).toString+",")).foldLeft("")(_+_).dropRight(1) + EOL)
        .foldLeft("")(_+_)
    }).foldLeft("")(_+_)+EOL
    returnString
  }

  def printGridResults:String = {
    val formatter = new GridStringFormatter
    formatter.printAsGrid(this.toString)
  }

  def summary:String = printGridResults
}

object UniversalGrid {
  def apply(gridGeometry:Seq[Int], nbSteps:Int, variableList:List[CalculationParametersIOLabels]):UniversalGrid = {
    val underlyingGrid:Map[CalculationParametersIOLabels,TimeStepVector] = variableList.map(v => (v, TimeStepVector(v, nbSteps, gridGeometry)(ClassTag(nbSteps.getClass),breeze.storage.Zero.IntZero))).toMap
    new UniversalGrid(gridGeometry, nbSteps, variableList, underlyingGrid)
  }
}