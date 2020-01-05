package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

case class SingleStepResults(valuesForAllLabels:Map[CalculationParametersIOLabels,Double])

object SingleStepResults {
  def apply(nonDoubleValuesPerLabel:Map[CalculationParametersIOLabels,AnyVal]){
    new SingleStepResults(nonDoubleValuesPerLabel.map(x => (x._1,(x._2).asInstanceOf[Double])))
  }
}