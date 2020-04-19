package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.ChainableCalculation

case class Cell(calculations:List[ChainableCalculation]=List()) extends GridElement {
  override val myDimension:Map[Int, GridElement] = Map()
  override def toString:String = "Cell"
}
