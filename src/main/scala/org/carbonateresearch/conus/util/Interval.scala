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

package org.carbonateresearch.conus.util

final case class Interval[T](private val valA:T, private val valB:T) extends Intervalable {

  val (min, max) = valA match {
    case v:Double => sortNumericalValues
    case v: Float => sortNumericalValues
    case v: BigDecimal  => sortNumericalValues
    case v: Byte => sortNumericalValues
    case v: Int => sortNumericalValues
    case v: BigInt => sortNumericalValues
    case _ => (valA,valB)
  }

  private def sortNumericalValues: (T,T) = {
    val mini = valA.asInstanceOf[BigDecimal]
    val maxi = valA.asInstanceOf[BigDecimal]

    if (mini<maxi)(mini.asInstanceOf[T],maxi.asInstanceOf[T])else(maxi.asInstanceOf[T],mini.asInstanceOf[T])
  }

  override def contains(value:Any): Boolean = {

    def containsNumericalValues: Boolean = {
      val valueBD = value.asInstanceOf[BigDecimal]
      val minBD = min.asInstanceOf[BigDecimal]
      val maxBD = max.asInstanceOf[BigDecimal]
      valueBD >= minBD && valueBD <= maxBD
    }

   val isContained:Boolean = min match {
      case v:Double => containsNumericalValues
      case v: Float => containsNumericalValues
      case v: BigDecimal  => containsNumericalValues
      case v: Byte => containsNumericalValues
      case v: Int => containsNumericalValues
      case v: BigInt => containsNumericalValues
      case _ => value == min || value == max
    }

    isContained
  }

  def toListWithNumberOfItem(nbItem:Int): List[T] = {
    min match {
      case v:Double => floatingPointIntervals(nbItem)
      case v: Float => floatingPointIntervals(nbItem)
      case v: BigDecimal  => floatingPointIntervals(nbItem)
      case v: Byte => indivisibleIntervals(nbItem)
      case v: Int => indivisibleIntervals(nbItem)
      case v: BigInt => indivisibleIntervals(nbItem)
      case _ => List(min, max)
    }
  }

  private def floatingPointIntervals(nbItem:Int):List[T] = {
    val maxi = max.asInstanceOf[BigDecimal]
    val mini = min.asInstanceOf[BigDecimal]
    val result = (0 to nbItem-1).toList.map(x => x*((maxi-mini/(nbItem.asInstanceOf[BigDecimal]))+mini))
    result.asInstanceOf[List[T]]
  }

  private def indivisibleIntervals(nbItem:Int):List[T] = {
    val maxi = max.asInstanceOf[BigInt]
    val mini = min.asInstanceOf[BigInt]
    val totalInterval = maxi-mini
    val indivisible = totalInterval % nbItem
    val increment = (totalInterval-indivisible)/nbItem
    val result = if(indivisible==0){
      (0 until nbItem-1).toList.map(x => maxi-x*increment).reverse} else {
      val interResults = (0 until nbItem-1).toList.map(x => x*increment+mini)
      val toAdd = (0 until nbItem-1).toList.map(x => if(x<indivisible)(x)else(indivisible)).asInstanceOf[List[BigInt]]
      val addInterResults:List[(BigInt,BigInt)] = interResults.zip(toAdd).toList
      addInterResults.map(t => t._1+t._2).toList
    }

    result.asInstanceOf[List[T]]
  }

}

