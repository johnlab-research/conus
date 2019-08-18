package org.carbonateresearch.conus.clumpedThermalModels

case object ClumpedEquations {
  val davies19_D47: Double => Double = (temp: Double) => 0.04028 * math.pow(10,6) / math.pow(temp+273.15,2) + 0.23776
  val davies19_T: Double => Double = (D47: Double) => math.pow(0.04028 * math.pow(10,6) / (D47 - 0.23776),0.5)-273.15
}
