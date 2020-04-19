package org.carbonateresearch.conus.grids

trait GridElement {
  def myDimension:Map[Int, GridElement]
  override def toString: String = {
    myDimension.toString()
  }
}
