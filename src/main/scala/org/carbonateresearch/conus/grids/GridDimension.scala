package org.carbonateresearch.conus.grids

case class GridDimension(override val myDimension:Map[Int, GridElement]) extends GridElement

object GridDimension {
  def apply(gridGeometry: List[Int]): GridDimension = {
    gridGeometry.size match {
      case 1 => {
        val thisDimension:Int = gridGeometry.head
        val cells = (0 until thisDimension)
        new GridDimension(cells.map(c => (c,Cell())).toMap)
      }
      case _ => {
        val thisDimension:Int = gridGeometry.head
        val cells = (0 until thisDimension)
        new GridDimension(cells.map(c => (c,GridDimension(gridGeometry.tail))).toMap)
      }
    }
  }
}
