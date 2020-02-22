package org.carbonateresearch.conus.implicits

case object steppedFunctions{
  type Step = Int
  type StepFunction[T] =  Step => T
}
