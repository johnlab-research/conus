package org.carbonateresearch.conus.util

final case class Interval(min:Double, max:Double){
  def contains(value:Double): Boolean = {
    value >= min && value <= max
  }

  def toListWithNumberOfItem(nbItem:Int): List[Double] = {
    val increment:Double = (max-min)/(nbItem.toDouble)
    (0 to nbItem-1).toList.map(x => x*increment+min)
  }
}

