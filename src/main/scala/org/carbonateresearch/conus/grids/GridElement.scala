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

trait GridElement {
  val vecSize:Int
  val coordinates:Seq[Int]

  def allGridCells:List[Seq[Int]] = {
    coordinates.size match {
      case 0 => List(Seq(1))
      case 1 => (0 until coordinates.head).map(x=>Seq(x)).toList
      case 2 => {
        val firstCoord:Seq[Int] = (0 until coordinates.head)
        val secondCoord:Seq[Int] = (0 until coordinates(1))
        for {
          f <- firstCoord
          s <- secondCoord
        } yield Seq(f,s)
      }.toList
      case 3 => {
        val firstCoord:Seq[Int] = (0 until coordinates.head)
        val secondCoord:Seq[Int] = (0 until coordinates(1))
        val thirdCoord:Seq[Int] = (0 until coordinates(2))
        for {
          f <- firstCoord
          s <- secondCoord
          t <- thirdCoord
        } yield Seq(f,s,t)
      }.toList
    }
  }

  def toString:String
  def setAtCell(value:Any, coordinates:Seq[Int]):Unit
  def setForAllCells(value:Any):Unit
  def getValueAtCell(coordinates:Seq[Int]):Any
}
