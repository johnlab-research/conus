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

import java.lang.System.lineSeparator
import breeze.linalg._

case class DataVector(vectorSize:Int,
                      coordinates:Seq[Int]) {
  private val EOL:String = lineSeparator()
  val underlyingVector:DenseVector[Any] = DenseVector.tabulate[Any](vectorSize){x => 0.asInstanceOf[Any]}

  def get[T](key:Int):T = {
    underlyingVector(key).asInstanceOf[T]
  }
  def set(key:Int,value:Any):Unit = {
    underlyingVector(key) = value
  }
  def setMany(keys:Seq[Int],values:Seq[Any]):Unit = {
    keys.foreach(k => underlyingVector(k) = values(keys.indexOf(k)))
  }

  override def toString():String = {
    val cString:String = coordinates.head+coordinates.tail.map(x => ","+x.toString).foldLeft("")(_+_)
    cString+(0 until vectorSize).map(v => ","+underlyingVector(v).toString).foldLeft("")(_+_)+EOL
  }
}
