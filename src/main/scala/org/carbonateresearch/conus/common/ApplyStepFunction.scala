package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.common.{SingleCalculationDescription, Step}
import org.carbonateresearch.conus.common.ModelVariable

case class ApplyStepFunction[T](equation:Step=>T){

  def storeResultAs(saveLabel:ModelVariable[T]): SingleCalculationDescription[T] = SingleCalculationDescription(equation,saveLabel)
}
