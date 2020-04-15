package org.carbonateresearch.conus.util

import org.carbonateresearch.conus.common.{ ModelVariable, ReturnDefaultValue}

object CommonModelVariables {
  val NumberOfSteps: ModelVariable[Int] = ModelVariable("Number of steps",
    defaultValue = 0,
    silent = false,
    ifNoValue = ReturnDefaultValue,
    precision = 0)

val Depth: ModelVariable[Double] =ModelVariable("Depth",
  defaultValue = 0,
  silent = false,
  ifNoValue = ReturnDefaultValue,
  precision = 2)
}
