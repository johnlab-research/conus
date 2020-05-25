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

import breeze.linalg._
import breeze.storage.Zero

import scala.reflect.ClassTag
import org.carbonateresearch.conus.grids.GridElement

 case class UGrid2D[T:ClassTag:Zero](gridGeometry:Seq[Int],
                      coordinates:Seq[Int],
                      zeroValue:T) extends GridElement {
  override val vecSize:Int = gridGeometry.head
  val rowSize:Int = gridGeometry(1)

   val underlyingGrid:DenseMatrix[T] = createMatrix(zeroValue)

   def createMatrix(zero:T): DenseMatrix[T] =
   {
     DenseMatrix.tabulate(vecSize,rowSize){case (i,j) => zero}
   }
   
  override def toString:String = {
    //(0 until gridGeometry.head).map(i => underlyingGrid(i).toString.foldLeft("")(_+_)
  "TODO"
  }


   def setAtCell(value:Any, coordinates:Seq[Int]):Unit = {
     underlyingGrid(coordinates.head,coordinates(1)) = value.asInstanceOf[T]
  }

   def setForAllCells(value:Any):Unit = {
    for {
      i <- (0 until vecSize)
      j <- (0 until rowSize)
    } yield (underlyingGrid(i,j) = value.asInstanceOf[T])
  }

  override def getValueAtCell(coordinates:Seq[Int]):Any = {
    underlyingGrid(coordinates.head, coordinates(1))
  }
}
