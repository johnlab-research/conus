package org.carbonateresearch.test

import org.carbonateresearch.conus.util.StepFunctions.Step

import org.carbonateresearch.conus.calculationparameters.CalculateStepValue

case class ApplyStepFunction[T](equation:Step=>T){

  def storeResultAs(saveLabel:ModelVariable[T]): SingleCalculationDescription[T] = SingleCalculationDescription(equation,saveLabel)
}


