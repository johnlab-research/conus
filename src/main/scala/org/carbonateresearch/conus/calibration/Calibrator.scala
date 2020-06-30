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

package org.carbonateresearch.conus.calibration

import org.carbonateresearch.conus.common.{CalculationParametersIOLabels, ModelVariable}
import scala.util.{Success, Try}
import math.pow

trait Calibrator{
  val coordinates:List[Seq[Int]]
  val step:Option[Int]
  def atCells(allCells:Seq[Int]*):Calibrator
  def atStep(stepNb:Int):Calibrator
  def label:CalculationParametersIOLabels
  def checkCalibration(value:Any):Boolean
  def squaredError(value:Any):Option[Double]
}
case class InRange[T](min:T, max:T,label:ModelVariable[T],coordinates:List[Seq[Int]]=List(Seq()), step:Option[Int]=None)
                     (implicit num:Numeric[T]) extends Calibrator{
  def atCells(allCells:Seq[Int]*):Calibrator = this.copy(coordinates=allCells.toList)
  def atStep(stepNb:Int):Calibrator = this.copy(step=Some(stepNb))
  def checkCalibration(value:Any):Boolean = {
    val attempt = Try {
      applyRules(value.asInstanceOf[T])
    }
    attempt match {
      case Success(v) => v
      case _ => false
    }
  }

  def squaredError(value:Any):Option[Double] = {
    val attempt = Try {
      val result = value.asInstanceOf[Double]
      val maxDouble = max.asInstanceOf[Double]
      val minDouble = min.asInstanceOf[Double]

      val target:Double = if(result > minDouble && result < maxDouble){result}
               else if(result < minDouble) {minDouble}
               else {maxDouble}
      pow((result - target),2)
    }
    attempt match {
      case Success(v) => Some(v)
      case _ => None
    }
  }

  private def applyRules(value:T):Boolean = num.min(max, value) == num.max(min, value)
}

case class LargerThan[T](min:T,label:ModelVariable[T],coordinates:List[Seq[Int]]=List(Seq()), step:Option[Int]=None)
                        (implicit num:Numeric[T])  extends Calibrator{
  def atCells(allCells:Seq[Int]*):Calibrator = this.copy(coordinates=allCells.toList)
  def atStep(stepNb:Int):Calibrator = this.copy(step=Some(stepNb))
  def checkCalibration(value:Any):Boolean = {
    val attempt = Try {
      applyRules(value.asInstanceOf[T])
    }
    attempt match {
      case Success(v) => v
      case _ => false
    }
  }

  def squaredError(value:Any):Option[Double] = {
    val attempt = Try {
      val result = value.asInstanceOf[Double]
      val minDouble = min.asInstanceOf[Double]
      val target = if(result-minDouble>0){result}else{minDouble}
      pow((result-target),2)
    }
    attempt match {
      case Success(v) => Some(v)
      case _ => None
    }
  }

  private def applyRules(value:T):Boolean = value == num.max(min, value)
}

case class SmallerThan[T](max:T,label:ModelVariable[T],coordinates:List[Seq[Int]]=List(Seq()), step:Option[Int]=None)
                         (implicit num:Numeric[T])  extends Calibrator{
  def atCells(allCells:Seq[Int]*):Calibrator = this.copy(coordinates=allCells.toList)
  def atStep(stepNb:Int):Calibrator = this.copy(step=Some(stepNb))
  def checkCalibration(value:Any):Boolean = {
    val attempt = Try {
      applyRules(value.asInstanceOf[T])
    }
    attempt match {
      case Success(v) => v
      case _ => false
    }
  }

  def squaredError(value:Any):Option[Double] = {
    val attempt = Try {
      val result = value.asInstanceOf[Double]
      val maxDouble = max.asInstanceOf[Double]
      val target = if(result-maxDouble<0){result}else{maxDouble}
      pow((result-target),2)
    }
    attempt match {
      case Success(v) => Some(v)
      case _ => None
    }
  }

  private def applyRules(value:T):Boolean = num.min(max, value) == value
}

case class ValueEqualTo[T](targetVal:T, label:ModelVariable[T], coordinates:List[Seq[Int]]=List(Seq()), step:Option[Int]=None)
                          (implicit num:Numeric[T])  extends Calibrator{
  def atCells(allCells:Seq[Int]*):Calibrator = this.copy(coordinates=allCells.toList)
  def atStep(stepNb:Int):Calibrator = this.copy(step=Some(stepNb))
  def checkCalibration(value:Any):Boolean = {
    val attempt = Try {
      applyRules(value.asInstanceOf[T])
    }
    attempt match {
      case Success(v) => v
      case _ => false
    }
  }

  def squaredError(value:Any):Option[Double] = {
    val attempt = Try {
      val result = value.asInstanceOf[Double]
      val target = targetVal.asInstanceOf[Double]
      pow((result-target),2)
    }
    attempt match {
      case Success(v) => Some(v)
      case _ => None
    }
  }
  private def applyRules(value:T):Boolean = targetVal == value
}