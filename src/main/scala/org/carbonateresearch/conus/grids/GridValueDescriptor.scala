package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.CalculationParametersIOLabels

trait GridValueDescriptor {
  def variableName:CalculationParametersIOLabels
def setOfValues:List[List[(Any,Seq[Int])]]
}
