package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.calculationparameters.parametersIO.CalculationParametersIOLabels

case class SingleStepResults(valuesForAllLabels:Map[CalculationParametersIOLabels,Double]){
  def valueForLabel(label:CalculationParametersIOLabels):Double = valuesForAllLabels(label)
  def addValue(label:CalculationParametersIOLabels, newValue:Double):SingleStepResults = SingleStepResults(valuesForAllLabels++Map((label,newValue)))
  def merge(otherResult: SingleStepResults): SingleStepResults = {
    SingleStepResults(valuesForAllLabels++otherResult.valuesForAllLabels)
  }
}

object SingleStepResults {
  def apply(nonDoubleValuesPerLabel:Map[CalculationParametersIOLabels,AnyVal]){
    new SingleStepResults(nonDoubleValuesPerLabel.map(x => (x._1,(x._2).asInstanceOf[Double])))
  }
}