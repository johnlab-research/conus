package org.carbonateresearch.conus.util

case object ±{
  def apply(valA: Double, valB: Double): Intervalable ={
    Interval(valA-valB, valA+valB)}
}
