package org.carbonateresearch.conus.util

case object ±{
  def apply(valA: Double, valB: Double): Interval ={
    Interval(valA-valB, valA+valB)}
}
