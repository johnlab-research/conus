package org.carbonateresearch.conus.grids

import scala.annotation.tailrec

case class Grid(theGrid:Map[Int,GridElement],gridGeometry:List[Int]) {
  override def toString: String = {
    "Grid with "+numberOfCells+" cells in "+gridGeometry.size+" dimensions"
  }
  def numberOfCells:Int = gridGeometry.product
}
object Grid {
  def apply(myGrids: List[Int]): Grid = {

    val thisDimension = myGrids.size match {
      case 1 => {
        val thisDimension: Int = myGrids.head
        val cells = (0 until thisDimension)
        cells.map(c => (c, Cell())).toMap
      }
      case _ => {
        val thisDimension: Int = myGrids.head
        val cells = (0 until thisDimension)
        cells.map(c => (c, GridDimension(myGrids.tail))).toMap
      }
    }
    new Grid(thisDimension,myGrids)
  }

  def apply(gridGeometry: Int*): Grid = {
    Grid(gridGeometry.toList)
  }


}
