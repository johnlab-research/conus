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

trait CalculationParametersIOLabels{
  override def toString = ""
  val silent: Boolean
  val name:String
  def formatValueAsString(value:Any):String
  def labelToValueFormattedString(v:Any):String ={
    if(!silent){
    val myString = formatValueAsString(v)
    this.name + ": " + myString + unitName + " | " }
    else {""}
  }
  def unitName: String = ""
  def value = 0.0
  def precision:Int = 2
  def defaultValue:Any
}

