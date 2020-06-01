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
import org.carbonateresearch.conus.grids.{GridElement}
import breeze.linalg._

case class UGrid1D(gridGeometry:Seq[Int],
                               coordinates:Seq[Int],
                               underlyingGrid:DenseVector[Object]) extends GridElement {
  override val vecSize:Int = gridGeometry.head

  override def toString:String = {
    ("[" + (0 until gridGeometry.head-1).map(i => underlyingGrid(i).toString() + ",").foldLeft("")(_ + _) +
      underlyingGrid(gridGeometry.head-1).toString() + "]")
  }


  override def setAtCell(value:Any, coordinates:Seq[Int]):Unit = {
    underlyingGrid(coordinates.head) = value.asInstanceOf[Object]
  }

  override def setForAllCells(value:Any):Unit = {
    (0 until vecSize).foreach(i => underlyingGrid(i) = value.asInstanceOf[Object])
  }

  override def getValueAtCell(coordinates:Seq[Int]):Any = {
    underlyingGrid(coordinates.head)
  }
}
