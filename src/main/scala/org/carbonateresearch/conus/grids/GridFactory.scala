package org.carbonateresearch.conus.grids
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.universal.UniversalGrid

import scala.annotation.tailrec

object GridFactory {
  def apply(gridGeometry:Seq[Int], nbSteps:Int, variableMap:Map[CalculationParametersIOLabels,Int]):Grid = {
    UniversalGrid(gridGeometry, nbSteps, variableMap)
  }
}
