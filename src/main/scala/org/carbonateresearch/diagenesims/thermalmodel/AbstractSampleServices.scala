package org.carbonateresearch.diagenesims.thermalmodel

abstract trait AbstractSampleServices {
  def getClosest[A: Numeric](num: A, list: List[A]): A = ???
  def depthForStep(age: Double, thermalHistory: ): Int = thermalHistory.age2Depth(age)

}
