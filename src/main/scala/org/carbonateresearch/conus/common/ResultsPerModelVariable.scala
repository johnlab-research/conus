package org.carbonateresearch.conus.common

case class ResultsPerModelVariable(private val dataContainer: Map[CalculationParametersIOLabels, Map[Int, Any]]) {
  def apply[T](k:ModelVariable[T]):Map[Int,T] = {
    dataContainer(k).asInstanceOf[Map[Int,T]]
  }

}
