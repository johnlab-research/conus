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

import StepFunctionUtils._
import org.carbonateresearch.conus.common.{Step,ModelVariable}

object CommonFunctions {

  def scaleFromMinToMaxByStep[T](min: T, max: T)(implicit num:Fractional[T]): StepFunction[T] = {
    import org.carbonateresearch.conus.util.CommonModelVariables.NumberOfSteps
    val myFunction: StepFunction[T] = (s: Step) => {

      val typedNbSteps = min match {
        case m: Double => NumberOfSteps(s).toDouble
        case m: Float => NumberOfSteps(s).toFloat
        case _ => NumberOfSteps(s).toDouble
      }

      val v = min match {
        case m: Double => s.stepNumber.toDouble
        case m: Float => s.stepNumber.toFloat
        case _ => s.stepNumber.toDouble
      }
      val increment = num.div(num.minus(max,min),num.minus(typedNbSteps.asInstanceOf[T],1.0.asInstanceOf[T]))
      num.minus(max, (num.times(v.asInstanceOf[T],increment)))}

    myFunction
  }

def interpolatedValue[T](v:ModelVariable[T], xyList: List[(T, T)])(implicit num:Fractional[T]):StepFunction[T] = {
  val stepEquation = (s: Step) => {
    val xValue = v(s)
    val xs = xyList.sorted.map(a => a._1)
    val ys = xyList.sorted.map(a => a._2)
    val firstNegative: Option[T] = getUpperBound(xValue,xs)
    var Index = 0
    firstNegative match {
      case Some(i) => {
        if (xs.indexOf(i) < xs.size && xs.indexOf(i) > 0) {
          Index = xs.indexOf(i)
        } else if (xs.indexOf(i) == 0) {
          Index = 1
        } else {
          Index = xs.indexOf(i) - 1
        }
      }
      case None => Index = xs.size - 1
    }
    val topY = ys(Index - 1)
    val topX = xs(Index - 1)
    val botY = ys(Index)
    val botX = xs(Index)
    num.plus(num.times(num.div(num.minus(botY,topY), num.minus(topX,botX)), num.minus(topX,xValue)),topY)
  }
stepEquation
}

  private def getUpperBound[T](value:T, xs:List[T])(implicit num:Fractional[T]): Option[T] = {
    val signedIndex = xs.map(x => if(num.compare((num.minus(value,x)),num.zero) > 0) {
      (xs.size-xs.indexOf(x)+1)
    } else {-1*(xs.size-xs.indexOf(x)+1)})
    val pairedVals = signedIndex.zip(xs).sorted
    if(pairedVals.head._1<=0){
      Some(pairedVals.head._2)
    } else {None}
  }
}
